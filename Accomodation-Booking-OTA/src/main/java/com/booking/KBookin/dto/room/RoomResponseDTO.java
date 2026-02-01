package com.booking.KBookin.dto.room;
import com.booking.KBookin.enumerate.property.RoomStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO implements Serializable {

    private Long id;
    private Long roomTypeId;
    private String roomTypeName;
    private String roomNumber;
    private RoomStatus roomStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastCleanedAt;

}