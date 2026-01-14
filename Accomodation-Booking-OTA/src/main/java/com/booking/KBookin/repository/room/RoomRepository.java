package com.booking.KBookin.repository.room;

import com.booking.KBookin.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(
            value = """
            SELECT *
            FROM rooms r
            WHERE r.room_status = 'AVAILABLE'
              AND r.room_type_id IN (:roomTypeIds)
        """,
            nativeQuery = true
    )
    List<Room> findAvailableByRoomTypeIds(@Param("roomTypeIds") List<Long> roomTypeIds);
}
