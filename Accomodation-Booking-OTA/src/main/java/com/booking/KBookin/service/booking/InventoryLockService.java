package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.booking.InventoryHoldResponse;
import com.booking.KBookin.dto.booking.LockRoomRequestDTO;
import com.booking.KBookin.dto.booking.LockRoomResponseDTO;

import java.net.URI;

public interface InventoryLockService {
    InventoryHoldResponse handleLockRoom(LockRoomRequestDTO request);
}
