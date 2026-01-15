package com.booking.KBookin.dto.booking;

import com.booking.KBookin.entity.room.RoomType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LockRoomRequestDTO {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private List<LockRoomRequestDetailDTO> lockRoomRequestDetail;
    private Long userId;


}

