package com.booking.KBookin.kafka.producer.review;

import com.booking.KBookin.dto.review.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.review.Topic.REVIEW_CREATED_EVENTS;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = REVIEW_CREATED_EVENTS;
    public void sendReviewCreatedEvent(ReviewResponseDTO reviewDTO) {
        log.info("Producing event for created review: {}", reviewDTO.getId());
        kafkaTemplate.send(TOPIC, reviewDTO);
    }
}
