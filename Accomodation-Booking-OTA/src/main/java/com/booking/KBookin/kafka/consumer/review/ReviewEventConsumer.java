package com.booking.KBookin.kafka.consumer.review;

import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.booking.KBookin.kafka.topic.review.Topic.REVIEW_CREATED_EVENTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewEventConsumer {
    private final ReviewService reviewService;
    @KafkaListener(
            topics = REVIEW_CREATED_EVENTS,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeReviewEvent(ReviewResponseDTO reviewDTO) {
        log.info("Received event for review id: {}. Updating property review ...", reviewDTO.getId());
        reviewService.updatePropertyRating(reviewDTO);
    }
}