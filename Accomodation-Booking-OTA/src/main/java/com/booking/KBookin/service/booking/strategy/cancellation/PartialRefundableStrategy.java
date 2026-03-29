package com.booking.KBookin.service.booking.strategy.cancellation;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.entity.property.CancellationPolicy;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PartialRefundableStrategy implements CancellationStrategy {
    @Override
    public CancellationPolicyType getType() {
        return CancellationPolicyType.PARTIAL;
    }

    @Override
    public BigDecimal calculateFee(BookingItem bookingItem, Booking booking) {
        CancellationPolicy policy = bookingItem.getRatePlan().getCancellationPolicy();
        LocalDateTime checkInDateTime = booking.getCheckIn().atStartOfDay();
        LocalDateTime deadline = checkInDateTime.minusHours(policy.getHoursBefore());

        if (LocalDateTime.now().isBefore(deadline)) {
            BigDecimal fixedFee = Optional.ofNullable(policy.getFixedFee()).orElse(BigDecimal.ZERO);
            BigDecimal percentageFee = bookingItem.getAmount()
                    .multiply(BigDecimal.valueOf(policy.getRefundPercentage()))
                    .divide(BigDecimal.valueOf(100));

            return fixedFee.multiply(BigDecimal.valueOf(bookingItem.getQuantity()))
                    .add(percentageFee);
        }
        
        // If past deadline, it acts like non-refundable or some other logic
        // Current logic in Cancellation.java suggests if it's NOT before the deadline, then fee is ZERO? 
        // Wait, looking at Cancellation.java lines 89-101, it ONLY adds fee if before deadline.
        // That seems counter-intuitive, usually fee applies IF YOU CANCEL LATE. 
        // Let's re-read Cancellation.java lines 89-100.
        // if (LocalDateTime.now().isBefore(cancellationDeadline)) { ... add fee ... }
        // This is weird. Usually "isAfter(deadline)" means fee applies. 
        // I will stick to the existing logic for now but might want to suggest a bug fix.
        return BigDecimal.ZERO;
    }
}
