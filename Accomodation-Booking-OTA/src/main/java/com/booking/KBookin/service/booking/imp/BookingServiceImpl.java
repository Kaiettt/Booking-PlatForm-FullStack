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
import com.booking.KBookin.mapper.booking.BookingMapper;
import com.booking.KBookin.mapper.booking.CheckinMapper;
import com.booking.KBookin.mapper.booking.CheckoutMapper;
import com.booking.KBookin.repository.booking.BookingRepository;
import com.booking.KBookin.repository.booking.CheckinRepository;
import com.booking.KBookin.repository.rate_plan.RatePlanRepository;
import com.booking.KBookin.service.booking.BookingService;
import com.booking.KBookin.service.booking.event.BookingCreatedEvent;
import com.booking.KBookin.service.booking.strategy.action.BookingActionStrategyFactory;
import com.booking.KBookin.service.booking.validation.BookingValidationChain;
import com.booking.KBookin.service.booking.RoomAssignmentService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private ApplicationEventPublisher eventPublisher;
    private BookingActionStrategyFactory bookingActionStrategyFactory;
    private BookingValidationChain validationChain;
    private RoomAssignmentService roomAssignmentService;
    @Transactional
    @Override
    public BookingResponse createBooking(CreateBookingRequestDTO request) {
        validationChain.validate(request);
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

        Booking booking = Booking.fromCreateRequest(request);

        Set<BookingItem> items = new LinkedHashSet<>();

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
        BookingResponse bookingResponse = this.bookingMapper.toResponse(savedBooking);
        this.bookingActionStrategyFactory.getStrategy(savedBooking.getPaymentMethod()).process(savedBooking);
        this.eventPublisher.publishEvent(new BookingCreatedEvent(this, bookingResponse));
        return bookingResponse;
    }

    @Transactional
    @Override
    public CheckinResponse handleCheckin(Long bookingId) {
        Booking booking = this.bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        Set<Room> selectedRooms = this.roomAssignmentService.assignRooms(booking);
        
        selectedRooms.forEach(room -> room.setRoomStatus(RoomStatus.OCCUPIED));
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
