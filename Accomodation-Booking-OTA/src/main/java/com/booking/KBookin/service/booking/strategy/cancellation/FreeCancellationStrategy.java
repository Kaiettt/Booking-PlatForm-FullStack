package com.booking.KBookin.service.booking.strategy.cancellation;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FreeCancellationStrategy implements CancellationStrategy {
    @Override
    public CancellationPolicyType getType() {
        return CancellationPolicyType.FREE;
    }

    @Override
    public BigDecimal calculateFee(BookingItem bookingItem, Booking booking) {
        return BigDecimal.ZERO;
    }
}
