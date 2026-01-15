package com.booking.KBookin.service.booking.imp;

import com.booking.KBookin.dto.booking.BookingItemRequestDTO;
import com.booking.KBookin.dto.checkin.CheckoutResponse;
import com.booking.KBookin.dto.booking.CreateBookingRequestDTO;
import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.checkin.CheckinResponse;
import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingGuest;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.entity.booking.CheckIn;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.booking.BookingStatus;
import com.booking.KBookin.enumerate.booking.CheckInStatus;
import com.booking.KBookin.enumerate.payment.PaymentMethod;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import com.booking.KBookin.enumerate.property.RoomStatus;
import com.booking.KBookin.kafka.producer.email.EmailBookingSuccessProducer;
import com.booking.KBookin.mapper.booking.BookingMapper;
import com.booking.KBookin.mapper.booking.CheckinMapper;
import com.booking.KBookin.mapper.booking.CheckoutMapper;
import com.booking.KBookin.repository.booking.BookingRepository;
import com.booking.KBookin.repository.booking.CheckinRepository;
import com.booking.KBookin.repository.rate_plan.RatePlanRepository;
import com.booking.KBookin.service.booking.BookingService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private RatePlanRepository ratePlanRepository;
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private RoomService roomService;
    private CheckinRepository checkinRepository;
    private CheckinMapper checkinMapper;
    private CheckoutMapper checkoutMapper;
    private EmailBookingSuccessProducer emailBookingSuccessProducer;
    @Transactional
    @Override
    public BookingResponse createBooking(CreateBookingRequestDTO request) {
        List<Long> ratePlanIds = request.getItems().stream()
                .map(BookingItemRequestDTO::getRatePlaneId)
                .toList();
        Map<Long, RatePlan> ratePlanMap =
                ratePlanRepository.findAllById(ratePlanIds)
                        .stream()
                        .collect(Collectors.toMap(
                                RatePlan::getId,
                                rp -> rp
                        ));

        Booking booking = Booking.builder()
                .user(User.builder()
                        .id(request.getUserId())
                        .build())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .paymentStatus(PaymentStatus.PENDING)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .specialRequest(request.getSpecialRequest())
                .guest(BookingGuest.builder()
                        .name(request.getGuest().getName())
                        .email(request.getGuest().getEmail())
                        .phone(request.getGuest().getPhone())
                        .nationality(request.getGuest().getNationality())
                        .build())
                .bookingItems(new ArrayList<>())
                .build();

        List<BookingItem> items = new ArrayList<>();

        for (BookingItemRequestDTO itemDto : request.getItems()) {
            BookingItem item = BookingItem.builder()
                    .booking(booking)
                    .roomType(RoomType.builder()
                            .id(itemDto.getRoomTypeId())
                            .build())
                    .quantity(itemDto.getQuantity())
                    .ratePlan(ratePlanMap.get(itemDto.getRatePlaneId()))
                    .build();
            item.calculateAmount();
            items.add(item);
        }
        booking.setBookingItems(items);
        booking.calculateTotalAmount();
        booking.handleBookingPaymentMethod();
        Booking savedBooking = this.bookingRepository.save(booking);
        if(savedBooking.getPaymentMethod() == PaymentMethod.CASH){
            this.roomService.releaseAllRoomLock(booking.getUser().getId());
        }
        else {
            this.roomService.extendRoomLock(booking.getUser().getId());
        }
        BookingResponse bookingResponse = this.bookingMapper.toResponse(savedBooking);
        if(savedBooking.getPaymentMethod() == PaymentMethod.CASH){
            this.emailBookingSuccessProducer.sendBookingSuccessEvent(bookingResponse);
        }
        return bookingResponse;
    }

    @Transactional
    @Override
    public CheckinResponse handleCheckin(Long bookingId) {
        Booking booking = this.bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        List<Long> roomTypeIds = booking.getBookingItems().stream()
                .map(BookingItem::getRoomType)
                .map(RoomType::getId)
                .toList();
        List<Room> rooms = this.roomService.findAvaibaleRoomsByRoomTypeIds(roomTypeIds);
        Map<Long, List<Room>> roomsByRoomTypeId = rooms.stream()
                .collect(Collectors.groupingBy(
                        room -> room.getRoomType().getId()
                ));
        Set<Room> selectedRooms = new HashSet<>();
        for(BookingItem bookingItem : booking.getBookingItems()){
            Long roomTypeId = bookingItem.getRoomType().getId();
            Integer quantity = bookingItem.getQuantity();

            List<Room> suitableRooms = roomsByRoomTypeId.get(roomTypeId);

            if (suitableRooms == null || suitableRooms.size() < quantity) {
                throw new IllegalStateException(
                        "Not enough available rooms for roomTypeId=" + roomTypeId
                );
            }

            for (int i = 0; i < quantity; i++) {
                Room room = suitableRooms.remove(0);
                selectedRooms.add(room);
            }
        }
        selectedRooms.forEach(room ->
                room.setRoomStatus(RoomStatus.OCCUPIED));
        this.roomService.updateAllRooms(selectedRooms);
        booking.setStatus(BookingStatus.CHECK_IN);
        this.bookingRepository.save(booking);
        CheckIn checkIn = CheckIn.builder()
                .checkInAt(LocalDateTime.now())
                .rooms(selectedRooms)
                .booking(booking)
                .status(CheckInStatus.CHECKED_IN)
                .build();

        CheckIn savedChekin = this.checkinRepository.save(checkIn);
        return this.checkinMapper.toResponse(savedChekin);
    }

    @Override
    public CheckoutResponse handleCheckout(Long bookingId) {
        CheckIn checkIn = this.checkinRepository.findByBookingIdForCheckout(bookingId)
                        .orElseThrow(() -> new EntityNotFoundException("Check in not found"));
        checkIn.handleCheckout();
        for(Room room : checkIn.getRooms()){
            room.setRoomStatus(RoomStatus.AVAILABLE);
        }
        this.roomService.updateAllRooms(checkIn.getRooms());
        CheckIn savedCheckin = this.checkinRepository.save(checkIn);
        return this.checkoutMapper.toResponse(savedCheckin);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        return this.bookingRepository.findProjectedById(id)
                .map(projection -> this.bookingMapper.toResponseFromProjection(projection))
                .orElseThrow(() -> new EntityNotFoundException("Booking not found " + id));
    }


}
