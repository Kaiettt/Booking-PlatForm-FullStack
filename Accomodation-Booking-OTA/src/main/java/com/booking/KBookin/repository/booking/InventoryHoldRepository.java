package com.booking.KBookin.repository.booking;

import com.booking.KBookin.entity.inventory.InventoryHold;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryHoldRepository extends JpaRepository<InventoryHold, Long> {
    @Query("""
    select distinct i
    from InventoryHold i
    join fetch i.inventoryHoldDetails d
    where i.user.id = :userId
    order by i.createdAt desc
""")
    List<InventoryHold> findByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );



    InventoryHold findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query("""
        delete from InventoryHold i
        where i.expiresAt < :now
    """)
    int deleteExpiredHolds(@Param("now") LocalDateTime now);
    @Query("""
    select distinct i
    from InventoryHold i
    join fetch i.inventoryHoldDetails d
    join fetch d.roomType rt
    where i.expiresAt < :now
""")
    List<InventoryHold> findExpiredHolds(@Param("now") LocalDateTime now);

//
//    @Modifying
//    @Transactional
//    @Query("""
//        DELETE FROM InventoryHold ih
//        WHERE ih.user.id = :userId
//          AND ih.roomType.id = :roomTypeId
//    """)
//    void releaseAllRoomLocks(@Param("userId") Long userId, @Param("roomTypeId") Long roomTypeId);
}

