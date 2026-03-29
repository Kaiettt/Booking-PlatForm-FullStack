package com.booking.KBookin.service.booking.imp;

import com.booking.KBookin.entity.booking.Booking;
import com.booking.KBookin.entity.booking.BookingItem;
import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.service.booking.RoomAssignmentService;
import com.booking.KBookin.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAssignmentServiceImpl implements RoomAssignmentService {
    private final RoomService roomService;

    @Override
    public Set<Room> assignRooms(Booking booking) {
        List<Long> roomTypeIds = booking.getBookingItems().stream()
                .map(BookingItem::getRoomType)
                .map(RoomType::getId)
                .toList();
        
        List<Room> availableRooms = roomService.findAvaibaleRoomsByRoomTypeIds(roomTypeIds);
        
        Map<Long, List<Room>> roomsByRoomTypeId = availableRooms.stream()
                .collect(Collectors.groupingBy(room -> room.getRoomType().getId()));
        
        Set<Room> selectedRooms = new HashSet<>();
        
        for (BookingItem bookingItem : booking.getBookingItems()) {
            Long roomTypeId = bookingItem.getRoomType().getId();
            Integer quantity = bookingItem.getQuantity();

            List<Room> suitableRooms = roomsByRoomTypeId.get(roomTypeId);

            if (suitableRooms == null || suitableRooms.size() < quantity) {
                throw new IllegalStateException("Not enough available rooms for roomTypeId=" + roomTypeId);
            }

            for (int i = 0; i < quantity; i++) {
                Room room = suitableRooms.remove(0);
                selectedRooms.add(room);
            }
        }
        
        return selectedRooms;
    }
}
