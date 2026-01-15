package com.booking.KBookin.kafka.producer.email;

import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.booking.CancellationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.booking.Topic.BOOKING_SUCCESS_EVENTS;
import static com.booking.KBookin.kafka.topic.booking.Topic.CANCEL_BOOKING_SUCCESS_EVENTS;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailCancelBookingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = CANCEL_BOOKING_SUCCESS_EVENTS;
    public void sendCancelBooking(CancellationResponse cancelResponse) {
        log.info("Producing event for cancel booking: with cancel id: {}", cancelResponse.getId());
        kafkaTemplate.send(TOPIC, cancelResponse);
    }
}
