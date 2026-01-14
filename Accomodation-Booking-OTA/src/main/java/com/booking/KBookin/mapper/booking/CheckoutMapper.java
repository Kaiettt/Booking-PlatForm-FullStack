package com.booking.KBookin.mapper.booking;

import com.booking.KBookin.dto.checkin.CheckoutResponse;
import com.booking.KBookin.dto.room.RoomResponse;
import com.booking.KBookin.entity.booking.CheckIn;
import com.booking.KBookin.entity.room.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CheckoutMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "booking.id", target = "bookingId")
    CheckoutResponse toResponse(CheckIn checkIn);

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "roomType.id", target = "roomTypeId")
    RoomResponse toRoomResponse(Room room);
}
