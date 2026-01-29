package com.booking.KBookin.dto.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressCreateDTO implements Serializable {
    @NotBlank(message = "Street address is required")
    private String streetAddress;

    private String ward;
    private String district;

    @NotBlank(message = "City is required")
    private String city;

    private String state;
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    @Valid
    private GeoLocationCreateDTO geo;
}