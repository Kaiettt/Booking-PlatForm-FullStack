package com.booking.KBookin.repository.projection.booking;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingProjection {
    Long getId();
    @Value("#{target.user.id}")
    Long getUserId();
    String getStatus();
    String getPaymentMethod();
    String getPaymentStatus();
    BigDecimal getTotalAmount();
    LocalDate getCheckIn();
    LocalDate getCheckOut();
    String getSpecialRequest();
    String getBookingReference();
    BookingGuestProjection getGuest();
    List<BookingItemProjection> getBookingItems();
    interface BookingGuestProjection {
        String getName();
        String getEmail();
        String getPhone();
        String getNationality();
    }
    interface BookingItemProjection {
        Long getId();
        @Value("#{target.roomType.id}")
        Long getRoomTypeId();
        @Value("#{target.roomType.name}")
        String getRoomTypeName();
        Integer getQuantity();
        BigDecimal getAmount();
        RatePlanProjection getRatePlan();
    }
    interface RatePlanProjection {
        Long getId();
        String getName();
        Double getPrice();
        String getPrepaymentType();
        List<PerkProjection> getPerks();
    }
    interface PerkProjection {
        Long getId();
        String getCode();
        String getName();
    }
}