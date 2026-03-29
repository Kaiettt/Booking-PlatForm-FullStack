package com.booking.KBookin.service.booking.strategy.cancellation;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.entity.property.CancellationPolicy;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class NonRefundableStrategy implements CancellationStrategy {
    @Override
    public CancellationPolicyType getType() {
        return CancellationPolicyType.NON_REFUNDABLE;
    }

    @Override
    public BigDecimal calculateFee(BookingItem bookingItem, Booking booking) {
        CancellationPolicy policy = bookingItem.getRatePlan().getCancellationPolicy();
        BigDecimal fixedFee = Optional.ofNullable(policy.getFixedFee()).orElse(BigDecimal.ZERO);
        
        return fixedFee.multiply(BigDecimal.valueOf(bookingItem.getQuantity()))
                .add(bookingItem.getAmount());
    }
}
