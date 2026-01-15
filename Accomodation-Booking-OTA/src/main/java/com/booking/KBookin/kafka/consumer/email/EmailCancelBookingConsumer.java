package com.booking.KBookin.kafka.consumer.email;

import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.booking.CancellationResponse;
import com.booking.KBookin.service.booking.BookingService;
import com.booking.KBookin.service.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.booking.Topic.BOOKING_SUCCESS_EVENTS;
import static com.booking.KBookin.kafka.topic.booking.Topic.CANCEL_BOOKING_SUCCESS_EVENTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailCancelBookingConsumer {
    private final BookingService bookingService;
    private final EmailService emailService;
    @KafkaListener(
            topics = CANCEL_BOOKING_SUCCESS_EVENTS,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeCancelBookingSuccessEmailEvent(CancellationResponse cancelResponse) {
        BookingResponse bookingResponse = this.bookingService.getBookingById(cancelResponse.getBookingId());
        log.info("Received event for cancel booking id: {}. sending email for cancel booking ", cancelResponse.getId());
        emailService.sendCancelBookingSuccess(cancelResponse,bookingResponse);
    }
}