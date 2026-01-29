package com.booking.KBookin.service.room;

import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.repository.projection.property.PropertyHostProjection;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RoomService {
    List<Room> findAvaibaleRoomsByRoomTypeIds(List<Long> roomTypeIds);

    void updateAllRooms(Set<Room> selectedRooms);

    void releaseAllRoomLock(Long userId);

    void extendRoomLock(Long userId);

    void releaseRoomInventory(Integer quantity, Long id, LocalDate checkIn, LocalDate checkOut);

    List<RoomTypeHostProjection> fetchPropertyRoomsById(long propertyId);


//    void releaseRooms(Integer quantity, Long roomTypeId, LocalDate checkIn, LocalDate checkOut);
//
//    void releaseAllRoomLock(Long userId, Long roomTypeId);
}
