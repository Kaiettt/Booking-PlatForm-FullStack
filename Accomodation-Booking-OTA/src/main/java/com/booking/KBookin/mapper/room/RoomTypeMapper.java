package com.booking.KBookin.mapper.room;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.rate_plan.PerkResponse;
import com.booking.KBookin.dto.rate_plan.RatePlanResponse;
import com.booking.KBookin.dto.room.RoomTypeDTO;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.rate.Perk;
import com.booking.KBookin.entity.room.RoomAmenity;
import com.booking.KBookin.entity.room.RoomFacility;
import com.booking.KBookin.entity.room.RoomType;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    // Ánh xạ RoomType sang RoomTypeDTO
    // SỬA LỖI: Thay target từ "roomAmenityNames" thành "roomAmenities"
    @Mapping(source = "amenities", target = "roomAmenities", qualifiedByName = "amenitySetToStringList")
    // SỬA LỖI: Thay target từ "roomFacilityNames" thành "roomFacilities"
    @Mapping(source = "facilities", target = "roomFacilities", qualifiedByName = "facilitySetToStringList")
    RoomTypeDTO toDto(RoomType rt);

    // Ánh xạ RatePlan sang RatePlanResponse
    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "prepaymentType", target = "prepaymentType", qualifiedByName = "enumToString")
    RatePlanResponse toRatePlanResponse(RatePlan rp);

    // Ánh xạ Perk sang PerkResponse
    PerkResponse toPerkResponse(Perk perk);

    // Ánh xạ media
    List<MediaDTO> toMediaDTOs(List<com.booking.KBookin.entity.media.RoomTypeMedia> media);


    // Custom Mappings:

    // 1. Map Set<Amenity Object> to List<String Name>
    @Named("amenitySetToStringList")
    @IterableMapping(elementTargetType = String.class)
    default List<String> amenitySetToStringList(Set<RoomAmenity> amenities) {
        return Optional.ofNullable(amenities)
                .orElseGet(Collections::emptySet)
                .stream()
                .map(RoomAmenity::getName)
                .toList();
    }

    @Named("facilitySetToStringList")
    @IterableMapping(elementTargetType = String.class)
    default List<String> facilitySetToStringList(Set<RoomFacility> facilities) {
        return Optional.ofNullable(facilities)
                .orElseGet(Collections::emptySet)
                .stream()
                .map(RoomFacility::getName)
                .toList();
    }

    // 2. Map BigDecimal to Double (có check null)
    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal price) {
        return price != null ? price.doubleValue() : null;
    }

    // 3. Map Enum to String (có check null) - PHƯƠNG THỨC MÀ PropertyMapper GỌI
    @Named("enumToString")
    default String enumToString(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }
}