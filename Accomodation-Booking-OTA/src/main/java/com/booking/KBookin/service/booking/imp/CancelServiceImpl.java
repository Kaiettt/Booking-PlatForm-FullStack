package com.booking.KBookin.service.booking.imp;

import com.booking.KBookin.dto.booking.CancelBookingRequestDTO;
import com.booking.KBookin.dto.booking.CancellationResponse;
import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.entity.booking.Cancellation;
import com.booking.KBookin.kafka.producer.email.EmailCancelBookingProducer;
import com.booking.KBookin.mapper.booking.CancellationMapper;
import com.booking.KBookin.repository.booking.BookingRepository;
import com.booking.KBookin.repository.booking.CancellationRepository;
import com.booking.KBookin.service.booking.CancelService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CancelServiceImpl implements CancelService {
    private BookingRepository bookingRepository;
    private CancellationRepository cancellationRepository;
    private CancellationMapper cancellationMapper;
    private RoomService roomService;
    private EmailCancelBookingProducer emailCancelBookingProducer;
    @Transactional
    @Override
    public CancellationResponse cancelBooking(CancelBookingRequestDTO request) {
        Booking booking = this.bookingRepository.findByIdForCancellation(request.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        Cancellation cancellation = Cancellation.builder()
                .booking(booking)
                .cancellationReason(request.getCancellationReason())
                .build();
        cancellation.handleCancelBooking(booking);
        Cancellation savedCancellation = this.cancellationRepository.save(cancellation);
        booking.getBookingItems().forEach(item ->
                this.roomService.releaseRoomInventory(
                        item.getQuantity(),
                        item.getRoomType().getId(),
                        booking.getCheckIn(),
                        booking.getCheckOut())
                );;
        this.bookingRepository.save(booking);
        CancellationResponse cancellationResponse = this.cancellationMapper.toResponse(savedCancellation);
        this.emailCancelBookingProducer.sendCancelBooking(cancellationResponse);
        return cancellationResponse;
    }
}
