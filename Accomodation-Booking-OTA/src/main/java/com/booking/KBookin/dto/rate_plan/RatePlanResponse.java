package com.booking.KBookin.dto.rate_plan;

import lombok.*;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RatePlanResponse implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private String prepaymentType;
    private List<PerkResponse> perks;
}