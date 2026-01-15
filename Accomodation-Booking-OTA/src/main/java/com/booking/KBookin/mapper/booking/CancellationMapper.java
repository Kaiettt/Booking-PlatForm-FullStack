package com.booking.KBookin.mapper.booking;

import com.booking.KBookin.dto.booking.CancellationResponse;
import com.booking.KBookin.entity.booking.Cancellation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CancellationMapper {

    CancellationMapper INSTANCE = Mappers.getMapper(CancellationMapper.class);

    @Mapping(source = "booking.id", target = "bookingId")
    CancellationResponse toResponse(Cancellation cancellation);
}