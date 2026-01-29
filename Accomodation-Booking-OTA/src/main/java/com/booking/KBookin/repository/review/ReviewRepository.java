package com.booking.KBookin.repository.review;

import com.booking.KBookin.entity.review.Review;
import com.booking.KBookin.repository.projection.review.ReviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<ReviewProjection> findByPropertyId(Long propertyId, Pageable pageable);
}
