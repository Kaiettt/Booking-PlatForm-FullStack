package com.booking.KBookin.dto.booking;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CancellationResponse {
    private Long id;
    private Long bookingId;
    private String canceledBy;
    private String cancellationReason;
    private Instant cancellationTime;
    private BigDecimal cancellationFee;
    private BigDecimal refundAmount;
    private String status;
}
