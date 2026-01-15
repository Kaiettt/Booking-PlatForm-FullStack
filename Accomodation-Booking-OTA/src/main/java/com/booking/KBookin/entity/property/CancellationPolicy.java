// src/main/java/com/booking/KBookin/entity/property/CancellationPolicy.java
package com.booking.KBookin.entity.property;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.enumerate.booking.CancellationPolicyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cancellation_policies")
public class CancellationPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       Policy Identity
       ========================= */
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /* =========================
       Policy Behavior
       ========================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CancellationPolicyType type;
    // FREE, PARTIAL, NON_REFUNDABLE

    /**
     * Cancellation window in hours before check-in.
     * Applies only to FREE and PARTIAL policies.
     */
    @Column(name = "hours_before")
    private Integer hoursBefore;

    /**
     * Refund percentage when cancellation is allowed.
     * 100 = full refund, 0 = no refund.
     */
    @Column(name = "refund_percentage")
    private Integer refundPercentage;

    /**
     * Flat cancellation fee (optional).
     * Used when fee is not percentage-based.
     */
    @Column(name = "fixed_fee", precision = 10, scale = 2)
    private BigDecimal fixedFee;
}
