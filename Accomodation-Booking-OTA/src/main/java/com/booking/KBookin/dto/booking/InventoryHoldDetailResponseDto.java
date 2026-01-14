package com.booking.KBookin.dto.booking;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class InventoryHoldDetailResponseDto {
    private Long roomTypeId;
    private String roomTypeName;
    private Integer quantity;
}
