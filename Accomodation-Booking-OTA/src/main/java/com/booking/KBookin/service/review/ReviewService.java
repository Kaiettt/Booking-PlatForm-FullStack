package com.booking.KBookin.service.review;
import org.springframework.data.domain.Pageable;
import java.util.List;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.review.ReviewCreateRequest;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.repository.projection.review.ReviewProjection;

public interface ReviewService {
    ReviewResponseDTO createNewReview(ReviewCreateRequest reviewCreateRequest);

    void updatePropertyRating(ReviewResponseDTO reviewDTO);

    List<ReviewResponseDTO> getAllReviewByPropertyId(Long propertyId);

    PageResponse<List<ReviewProjection>> fetchPropertyReviewsById(long propertyId, Pageable pageable);
}
