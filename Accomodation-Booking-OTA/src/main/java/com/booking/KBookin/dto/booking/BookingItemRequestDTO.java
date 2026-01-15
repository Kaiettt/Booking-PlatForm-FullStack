package com.booking.KBookin.dto.booking;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingItemRequestDTO {

    @NotNull(message =  "Room Type is required")
    private Long roomTypeId;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    @NotNull(message = "Rate Plan is required")
    private Long ratePlaneId;
}