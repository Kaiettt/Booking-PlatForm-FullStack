package com.booking.KBookin.dto.booking;

import com.booking.KBookin.enumerate.booking.InventoryHoldStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class InventoryHoldResponse {

    private Long id;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDateTime expiresAt;

    private InventoryHoldStatus status;

    private Long userId;

    private List<InventoryHoldDetailResponseDto> details;


}