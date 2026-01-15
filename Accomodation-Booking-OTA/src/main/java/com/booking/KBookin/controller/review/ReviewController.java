package com.booking.KBookin.controller.review;

import com.booking.KBookin.dto.review.ReviewCreateRequest;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewCreateRequest reviewCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.reviewService.createNewReview(reviewCreateRequest));
    }
}
