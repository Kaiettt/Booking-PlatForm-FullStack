package com.booking.KBookin.dto.search;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SearchDetailPropertyDTO(
        Long propertyId,
        LocalDate checkingDate,
        LocalDate checkoutDate,
        Integer adults,
        Integer children,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minRating,
        String roomType,
        List<String> roomFacilities
) {}
