package com.booking.KBookin.dto.rate_plan;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PerkResponse implements Serializable {

    private Long id;
    private String code;
    private String name;
}