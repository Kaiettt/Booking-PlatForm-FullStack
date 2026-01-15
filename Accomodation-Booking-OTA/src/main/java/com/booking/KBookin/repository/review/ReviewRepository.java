package com.booking.KBookin.repository.review;

import com.booking.KBookin.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
