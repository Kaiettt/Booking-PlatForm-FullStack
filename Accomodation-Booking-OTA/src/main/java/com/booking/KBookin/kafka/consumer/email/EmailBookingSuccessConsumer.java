package com.booking.KBookin.kafka.consumer.email;

import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.service.notification.EmailService;
import com.booking.KBookin.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.booking.Topic.BOOKING_SUCCESS_EVENTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailBookingSuccessConsumer {
    private final EmailService emailService;
    @KafkaListener(
            topics = BOOKING_SUCCESS_EVENTS,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeBookingSuccessEmailEvent(BookingResponse bookingResponse) {
        log.info("Received event for booking id: {}. sending email for booking ", bookingResponse.getId());
        emailService.sendBookingSuccess(bookingResponse);
    }
}