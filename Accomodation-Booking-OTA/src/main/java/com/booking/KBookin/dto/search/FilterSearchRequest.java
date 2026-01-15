package com.booking.KBookin.dto.search;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FilterSearchRequest(
        String city,
        LocalDate checkingDate,
        LocalDate checkoutDate,
        Integer adults,
        Integer children,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        List<String> propertyTypes,
        Integer minRating,
        List<String> propertyAmenities,
        List<String> propertyFacilities,
        List<String> roomFacilities
) {}

