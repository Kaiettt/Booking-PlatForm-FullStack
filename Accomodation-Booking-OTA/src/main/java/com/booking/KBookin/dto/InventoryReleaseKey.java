package com.booking.KBookin.dto;

import java.time.LocalDate;

public record InventoryReleaseKey(
        Long roomTypeId,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
