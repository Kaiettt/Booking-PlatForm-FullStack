package com.booking.KBookin.service.booking.strategy.cancellation;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;

import java.math.BigDecimal;

public interface CancellationStrategy {
    CancellationPolicyType getType();
    BigDecimal calculateFee(BookingItem bookingItem, Booking booking);
}
