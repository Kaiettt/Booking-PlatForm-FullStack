package com.booking.KBookin.service.review.impl;

import com.booking.KBookin.dto.PageResponse;
import com.booking.KBookin.dto.review.ReviewCreateRequest;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.review.Review;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.kafka.producer.review.ReviewEventProducer;
import com.booking.KBookin.mapper.review.ReviewMapper;
import com.booking.KBookin.repository.projection.review.ReviewProjection;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.review.ReviewRepository;
import com.booking.KBookin.repository.user.UserRepository;
import com.booking.KBookin.service.review.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewEventProducer reviewEventProducer;

    @Transactional
    @Override
    public ReviewResponseDTO createNewReview(ReviewCreateRequest reviewCreateRequest) {
        User user = this.userRepository.findById(reviewCreateRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Property property = this.propertyRepository.findById(reviewCreateRequest.propertyId())
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));

        Review review = this.reviewMapper.toEntity(reviewCreateRequest);
        review.setUser(user);
        review.setProperty(property);
        ReviewResponseDTO responseDTO = this.reviewMapper.toResponseDTO(this.reviewRepository.save(review));
        reviewEventProducer.sendReviewCreatedEvent(responseDTO);
        return responseDTO;
    }

    @Override
    public void updatePropertyRating(ReviewResponseDTO reviewDTO) {
        Property property = this.propertyRepository.findById(reviewDTO.getPropertyId())
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));
        property.recalculateRating(reviewDTO.getRating());
        this.propertyRepository.save(property);
    }

    @Override
    public List<ReviewResponseDTO> getAllReviewByPropertyId(Long propertyId) {
        return null;
    }

    @Override
    public PageResponse<List<ReviewProjection>> fetchPropertyReviewsById(long propertyId, Pageable pageable) {
        Page<ReviewProjection> reviewsPage = this.reviewRepository.findByPropertyId(propertyId, pageable);

        return PageResponse.<List<ReviewProjection>>builder()
                .data(reviewsPage.getContent())
                .totalElements(reviewsPage.getTotalElements())
                .totalPages(reviewsPage.getTotalPages())
                .currentPage(reviewsPage.getNumber())
                .build();
    }
}
