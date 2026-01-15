package com.booking.KBookin.entity.location;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation implements Serializable {
    @Column(precision = 9, scale = 6)
    private BigDecimal lat;

    @Column(precision = 9, scale = 6)
    private BigDecimal lng;
}