package com.booking.KBookin.dto.booking;

import com.booking.KBookin.dto.rate_plan.RatePlanResponse;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingItemResponse implements Serializable {

    private Long id;

    private Long roomTypeId;
    private String roomTypeName;

    private Integer quantity;

    private BigDecimal amount;

    private RatePlanResponse ratePlan;
}