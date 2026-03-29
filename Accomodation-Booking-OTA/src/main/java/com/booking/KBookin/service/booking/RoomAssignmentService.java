package com.booking.KBookin.service.booking;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.room.Room;
import java.util.Set;

public interface RoomAssignmentService {
    Set<Room> assignRooms(Booking booking);
}
