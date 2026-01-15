package com.booking.KBookin.mapper.inventory;

import com.booking.KBookin.dto.booking.InventoryHoldDetailResponseDto;
import com.booking.KBookin.dto.booking.InventoryHoldResponse;
import com.booking.KBookin.entity.inventory.InventoryHold;
import com.booking.KBookin.entity.inventory.InventoryHoldDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryHoldMapper {

    /* =======================
       ENTITY → RESPONSE DTO
       ======================= */

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "inventoryHoldDetails", target = "details")
    InventoryHoldResponse toResponse(InventoryHold inventoryHold);

    /* =======================
       DETAIL → DETAIL DTO
       ======================= */

    @Mapping(source = "roomType.id", target = "roomTypeId")
    @Mapping(source = "roomType.name", target = "roomTypeName")
    InventoryHoldDetailResponseDto toDetailResponse(InventoryHoldDetail detail);

    List<InventoryHoldDetailResponseDto> toDetailResponses(
            List<InventoryHoldDetail> details
    );
}
