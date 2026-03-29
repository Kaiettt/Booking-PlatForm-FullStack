package com.booking.KBookin.service.room;

import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import com.booking.KBookin.dto.room.RoomTypeFilterDTO;
import java.util.List;

public interface RoomSearchService {
    List<Room> findAvaibaleRoomsByRoomTypeIds(List<Long> roomTypeIds);
    List<RoomTypeHostProjection> fetchPropertyRoomsById(long propertyId, RoomTypeFilterDTO filter);
    RoomTypeHostProjection fetchRoomTypeById(Long id);
}
