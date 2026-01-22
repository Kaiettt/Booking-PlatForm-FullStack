package com.booking.KBookin.service.review;

import java.util.List;

import com.booking.KBookin.dto.review.ReviewCreateRequest;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import jakarta.validation.Valid;

public interface ReviewService {
    ReviewResponseDTO createNewReview(ReviewCreateRequest reviewCreateRequest);

    void updatePropertyRating(ReviewResponseDTO reviewDTO);

    List<ReviewResponseDTO> getAllReviewByPropertyId(Long propertyId);
}
