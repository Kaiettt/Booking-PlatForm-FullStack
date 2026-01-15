package com.booking.KBookin.kafka.producer.email;

import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.booking.Topic.BOOKING_SUCCESS_EVENTS;
import static com.booking.KBookin.kafka.topic.review.Topic.REVIEW_CREATED_EVENTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailBookingSuccessProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = BOOKING_SUCCESS_EVENTS;
    public void sendBookingSuccessEvent(BookingResponse bookingResponse) {
        log.info("Producing event for created booking: {}", bookingResponse.getId());
        kafkaTemplate.send(TOPIC, bookingResponse);
    }
}
