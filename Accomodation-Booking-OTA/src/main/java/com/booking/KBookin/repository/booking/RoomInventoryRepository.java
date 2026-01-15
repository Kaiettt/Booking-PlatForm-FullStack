package com.booking.KBookin.repository.booking;

import com.booking.KBookin.entity.inventory.RoomInventory;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT ri FROM RoomInventory ri
        WHERE ri.roomType.id = :roomTypeId
          AND ri.date >= :checkInDate
          AND ri.date < :checkOutDate
          AND ri.availableRooms >= :quantity
        ORDER BY ri.date ASC
    """)
    List<RoomInventory> findAndLockByRoomTypeIdAndDateRange(
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("quantity") Integer quantity
    );

    @Modifying
    @Query("""
            UPDATE RoomInventory ri
            SET ri.availableRooms = ri.availableRooms + :quantity
            WHERE ri.roomType.id = :roomTypeId
            AND ri.date >= :checkInDate
            AND ri.date < :checkOutDate
            """)
    void updateAfterReleaseHolds(
            @Param("quantity") Integer quantity,
            @Param("roomTypeId") Long roomTypeId,
            @Param("checkInDate") LocalDate checkInDate, // Fixed name
            @Param("checkOutDate") LocalDate checkOutDate  // Fixed spelling: chek -> check
    );
}