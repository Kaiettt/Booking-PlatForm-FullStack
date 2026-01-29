package com.booking.KBookin.repository.projection.review;

import org.springframework.beans.factory.annotation.Value;

public interface ReviewProjection {
    Long getId();
    Integer getRating();
    String getComment();
    @Value("#{target.user.fullName}")
    String getUserName();
    @Value("#{target.property.id}")
    Long getPropertyId();
}