package com.booking.KBookin.service.room;

import java.time.LocalDate;

public interface InventoryService {
    void releaseAllRoomLock(Long userId);
    void extendRoomLock(Long userId);
    void releaseRoomInventory(Integer quantity, Long roomTypeId, LocalDate checkIn, LocalDate checkOut);
}
