package com.booking.KBookin.dto.room;

import com.booking.KBookin.enumerate.property.RoomStatus;

import lombok.*;

@EqualsAndHashCode
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    private Long roomId;
    private String roomNumber;
    private Long roomTypeId;
    private RoomStatus roomStatus;
}
