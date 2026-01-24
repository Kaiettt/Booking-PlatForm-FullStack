package com.booking.KBookin.service.booking.imp;

import com.booking.KBookin.dto.booking.InventoryHoldResponse;
import com.booking.KBookin.dto.booking.LockRoomRequestDTO;
import com.booking.KBookin.dto.booking.LockRoomRequestDetailDTO;
import com.booking.KBookin.entity.inventory.InventoryHold;
import com.booking.KBookin.entity.inventory.InventoryHoldDetail;
import com.booking.KBookin.entity.inventory.RoomInventory;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.booking.InventoryHoldStatus;
import com.booking.KBookin.exception.BusinessProcessException;
import com.booking.KBookin.mapper.inventory.InventoryHoldMapper;
import com.booking.KBookin.repository.booking.InventoryHoldRepository;
import com.booking.KBookin.repository.booking.RoomInventoryRepository;
import com.booking.KBookin.service.booking.InventoryLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.booking.KBookin.config.Common.INVENTORY_HOLD_EXPIRE_TIME_SECOND;

@Service
@RequiredArgsConstructor
public class InventoryLockServiceImpl implements InventoryLockService {

    private final InventoryHoldRepository inventoryHoldRepository;
    private final RoomInventoryRepository roomInventoryRepository;
    private final InventoryHoldMapper inventoryHoldMapper;

    @Transactional
    @Override
    public InventoryHoldResponse handleLockRoom(LockRoomRequestDTO request) {

        if (!request.getCheckIn().isBefore(request.getCheckOut())) {
            throw new BusinessProcessException(
                    "Check-out must be after check-in"
            );
        }

        Map<Long, Integer> roomTypeQuantityMap = aggregateRoomRequirements(request);
        LocalDate checkInDate = request.getCheckIn().toLocalDate();
        LocalDate checkOutDate = request.getCheckOut().toLocalDate();
        List<RoomInventory> lockedInventories = this.processInventoryDeduction(roomTypeQuantityMap,checkInDate,checkOutDate);
        roomInventoryRepository.saveAll(lockedInventories);


        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(INVENTORY_HOLD_EXPIRE_TIME_SECOND);

        InventoryHold inventoryHold = InventoryHold.builder()
                .user(User.builder()
                        .id(request.getUserId())
                        .build())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .expiresAt(expiresAt)
                .status(InventoryHoldStatus.SOFT_LOCK)
                .build();

        for (Map.Entry<Long, Integer> entry : roomTypeQuantityMap.entrySet()) {
            Long roomTypeId = entry.getKey();
            Integer quantity = entry.getValue();
            InventoryHoldDetail detail = InventoryHoldDetail.builder()
                    .roomType(RoomType.builder()
                            .id(roomTypeId)
                            .build())
                    .quantity(quantity)
                    .build();

            inventoryHold.addDetail(detail);
        }

        InventoryHold savedHold =
                inventoryHoldRepository.save(inventoryHold);

        return inventoryHoldMapper.toResponse(savedHold);
    }

    private Map<Long, Integer> aggregateRoomRequirements(LockRoomRequestDTO request) {
        return request.getLockRoomRequestDetail().stream()
                .collect(Collectors.toMap(
                        LockRoomRequestDetailDTO::getRoomTypeId,
                        LockRoomRequestDetailDTO::getQuantity,
                        Integer::sum
                ));
    }

    private List<RoomInventory>  processInventoryDeduction(Map<Long, Integer> roomTypeQuantityMap, LocalDate checkInDate, LocalDate checkOutDate){
        List<RoomInventory> lockedInventories = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : roomTypeQuantityMap.entrySet()) {
            Long roomTypeID = entry.getKey();
            Integer quantity = entry.getValue();
            List<RoomInventory> inventories =
                    roomInventoryRepository.findAndLockByRoomTypeIdAndDateRange(
                            roomTypeID,
                            checkInDate,
                            checkOutDate,
                            quantity
                    );
            if (inventories.isEmpty()) {
                throw new BusinessProcessException(
                        "Not enough inventory for roomTypeId: " + entry.getKey()
                );
            }
            lockedInventories.addAll(inventories);
        }


        long totalDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        long expectedRecords = totalDays * roomTypeQuantityMap.size();

        if (lockedInventories.size() != expectedRecords) {
            throw new BusinessProcessException(
                    "Missing inventory data for the specified date range"
            );
        }
        for (RoomInventory inventory : lockedInventories) {
            int quantity = roomTypeQuantityMap.get(inventory.getRoomType().getId());
            inventory.decreaseAvailability(quantity);
        }
        return lockedInventories;
    }

}
