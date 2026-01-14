package com.booking.KBookin.entity.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String ward;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Embedded
    private GeoLocation geo;

    private String fullAddress;
    public String getFullAddress() {
        return String.join(", ",
            streetAddress,
            ward,
            district,
            city,
            state,
            country
        ) + (postalCode != null && !postalCode.isBlank() ? " " + postalCode : "");
    }
}