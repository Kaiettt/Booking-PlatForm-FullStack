package com.booking.KBookin.dto.room;

import com.booking.KBookin.enumerate.property.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequestDTO implements Serializable {

    @NotNull(message = "Room Type ID is required")
    private Long roomTypeId;

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number must be less than 20 characters")
    private String roomNumber;

    private RoomStatus roomStatus;
}