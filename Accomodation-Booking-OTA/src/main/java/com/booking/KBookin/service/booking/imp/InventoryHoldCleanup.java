package com.booking.KBookin.service.booking.imp;

import com.booking.KBookin.dto.InventoryReleaseKey;
import com.booking.KBookin.entity.inventory.InventoryHold;
import com.booking.KBookin.entity.inventory.InventoryHoldDetail;
import com.booking.KBookin.entity.inventory.RoomInventory;
import com.booking.KBookin.repository.booking.InventoryHoldRepository;
import com.booking.KBookin.repository.booking.RoomInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryHoldCleanup {
    private final InventoryHoldRepository inventoryHoldRepository;
    private final RoomInventoryRepository roomInventoryRepository;
    @Transactional
    @Scheduled(fixedDelayString = "${inventory.cleanup.delay-ms:60000}")
    public void cleanupExpiredInventoryHolds(){
        LocalDateTime now = LocalDateTime.now();
        List<InventoryHold> expiredHolds = this.inventoryHoldRepository.findExpiredHolds(now);
        if(expiredHolds.isEmpty()){
            return;
        }
        log.info("Found {} expired inventory holds to process.", expiredHolds.size());
        Map<InventoryReleaseKey,Integer> releaseMap = new HashMap<>();

        for(InventoryHold inventoryHold: expiredHolds){
            for(InventoryHoldDetail inventoryHoldDetail:inventoryHold.getInventoryHoldDetails()){
                InventoryReleaseKey key = new InventoryReleaseKey(inventoryHoldDetail.getRoomType().getId(),inventoryHold.getCheckIn().toLocalDate(),inventoryHold.getCheckOut().toLocalDate());
                releaseMap.merge(key,inventoryHoldDetail.getQuantity(),Integer::sum);
            }
        }
        for(Map.Entry<InventoryReleaseKey,Integer> entry : releaseMap.entrySet()){
            InventoryReleaseKey inventoryReleaseKey = entry.getKey();
            Integer quantity = entry.getValue();
            this.roomInventoryRepository.updateAfterReleaseHolds(quantity,inventoryReleaseKey.roomTypeId(),inventoryReleaseKey.checkInDate(),inventoryReleaseKey.checkOutDate());
        }
        List<Long> holdIds = expiredHolds.stream()
                .map(InventoryHold::getId)
                .toList();
        log.info("Successfully deleted {} expired hold records.", holdIds.size());
        this.inventoryHoldRepository.deleteAllById(holdIds);
    }

}
