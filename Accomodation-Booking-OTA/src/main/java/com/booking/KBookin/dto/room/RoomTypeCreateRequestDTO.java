package com.booking.KBookin.dto.room;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeCreateRequestDTO implements Serializable {

    @NotNull(message = "Property Id is required")
    private Long propertyId;

    @NotBlank(message = "Room name is required")
    private String name;

    @Min(value = 1, message = "Max adults must be at least 1")
    private Integer maxAdults;

    @Min(value = 0)
    private Integer maxChildren;

    @Positive(message = "Max guests must be a positive number")
    private Integer maxGuest;

    @Positive(message = "Size must be greater than 0")
    private Integer sizeM2;

    private String bedType;

    private String viewType;

    private Boolean smokingAllowed;

    @NotNull(message = "Total rooms count is required")
    @Min(0)
    private Integer totalRooms;

    private Set<Long> amenityIds;

    private Set<Long> facilityIds;
}