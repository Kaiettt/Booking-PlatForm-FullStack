package com.booking.KBookin.dto.booking;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LockRoomRequestDetailDTO{
    private Long roomTypeId;
    private Integer quantity;
}
