package com.booking.KBookin.service.room.impl;

import com.booking.KBookin.entity.inventory.InventoryHold;
import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.repository.booking.InventoryHoldRepository;
import com.booking.KBookin.repository.booking.RoomInventoryRepository;
import com.booking.KBookin.repository.room.RoomRepository;
import com.booking.KBookin.repository.room.RoomTypeRepository;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private RoomTypeRepository roomTypeRepository;
    private RoomRepository roomRepository;
    private RoomInventoryRepository roomInventoryRepository;
    private InventoryHoldRepository inventoryHoldRepository;
    @Override
    public List<Room> findAvaibaleRoomsByRoomTypeIds(List<Long> roomTypeIds) {
        return this.roomRepository.findAvailableByRoomTypeIds(roomTypeIds);
    }

    @Override
    public void updateAllRooms(Set<Room> selectedRooms) {
        this.roomRepository.saveAll(selectedRooms);
    }

    @Override
    public void releaseAllRoomLock(Long userId) {
        InventoryHold inventoryHold = this.inventoryHoldRepository.findTopByUser_IdOrderByCreatedAtDesc(userId);
        if(inventoryHold == null){
            throw new EntityNotFoundException("Hold not found");
        }
        this.inventoryHoldRepository.deleteById(inventoryHold.getId());
    }

    @Override
    public void extendRoomLock(Long userId) {
        InventoryHold inventoryHold =
                this.inventoryHoldRepository.findByUserId(userId, PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElse(null);

        if(inventoryHold == null){
            throw new EntityNotFoundException("Room Lock not found");
        }
        inventoryHold.extendLock();
        this.inventoryHoldRepository.save(inventoryHold);
    }

    @Transactional
    @Override
    public void releaseRoomInventory(Integer quantity, Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        this.roomInventoryRepository.updateAfterReleaseHolds(quantity,roomTypeId,checkIn,checkOut);
    }

}
