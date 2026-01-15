package com.booking.KBookin.entity.booking;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.property.CancellationPolicy;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.enumerate.booking.BookingStatus;
import com.booking.KBookin.enumerate.booking.CanceledBy;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import com.booking.KBookin.enumerate.booking.CancellationStatus;
import com.booking.KBookin.enumerate.payment.PaymentStatus;
import jakarta.persistence.OneToOne;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "cancellations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "booking_id")
        }
)
public class Cancellation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, updatable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "canceled_by", nullable = false, length = 20)
    private CanceledBy canceledBy;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "cancellation_time", nullable = false)
    private Instant cancellationTime;

    @Column(name = "cancellation_fee", precision = 10, scale = 2)
    private BigDecimal cancellationFee;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CancellationStatus status;


    public void handleCancelBooking(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.REFUNDED);
        BigDecimal totalCancellationFee = BigDecimal.ZERO;

        for (BookingItem bookingItem : booking.getBookingItems()) {
            RatePlan ratePlan = bookingItem.getRatePlan();
            CancellationPolicy cancellationPolicy = ratePlan.getCancellationPolicy();
            BigDecimal itemAmount = bookingItem.getAmount();

            switch (cancellationPolicy.getType()) {

                case NON_REFUNDABLE -> {
                    BigDecimal fixedFee = Optional.ofNullable(cancellationPolicy.getFixedFee())
                            .orElse(BigDecimal.ZERO);
                    totalCancellationFee = totalCancellationFee
                            .add(fixedFee.multiply(BigDecimal.valueOf(bookingItem.getQuantity())))
                            .add(itemAmount);
                }

                case PARTIAL -> {
                    LocalDateTime checkInDateTime = booking.getCheckIn().atStartOfDay();
                    LocalDateTime cancellationDeadline =
                            checkInDateTime.minusHours(cancellationPolicy.getHoursBefore());

                    if (LocalDateTime.now().isBefore(cancellationDeadline)) {
                        BigDecimal fixedFee = Optional.ofNullable(cancellationPolicy.getFixedFee())
                                .orElse(BigDecimal.ZERO);

                        BigDecimal percentageFee = itemAmount
                                .multiply(BigDecimal.valueOf(cancellationPolicy.getRefundPercentage()))
                                .divide(BigDecimal.valueOf(100));

                        totalCancellationFee = totalCancellationFee
                                .add(fixedFee.multiply(BigDecimal.valueOf(bookingItem.getQuantity())))
                                .add(percentageFee);
                    }
                }

                case FREE -> {
                }
                default -> throw new IllegalStateException("Unexpected value: " + cancellationPolicy.getType());
            }
        }

        // Compute total refund once after summing all booking items
        BigDecimal totalRefund = booking.getTotalAmount().subtract(totalCancellationFee)
                .max(BigDecimal.ZERO);

        // Update Cancellation entity
        this.canceledBy = CanceledBy.GUEST;
        this.cancellationFee = totalCancellationFee;
        this.refundAmount = totalRefund;
        this.cancellationTime = Instant.now();
        this.status = CancellationStatus.COMPLETED;
    }

}
