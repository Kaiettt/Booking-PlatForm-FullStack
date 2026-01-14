package com.booking.KBookin.dto.checkin;

import com.booking.KBookin.dto.room.RoomResponse;
import com.booking.KBookin.enumerate.booking.CheckInStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CheckinResponse {
    private Long id;
    private Long bookingId;
    private CheckInStatus status;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    private List<RoomResponse> rooms;
}
