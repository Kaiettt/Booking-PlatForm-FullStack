package com.booking.KBookin.dto.search;

import java.time.LocalDate;

public record SearchRequestDTO(
        String city,
        LocalDate checkingDate,
        LocalDate checkoutDate,
        int adults,
        int children
) {}