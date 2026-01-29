package com.booking.KBookin.repository.room;

import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query("SELECT rt FROM RoomType rt WHERE rt.property.id = :propertyId")
    List<RoomTypeHostProjection> findRoomsByPropertyId(@Param("propertyId") Long propertyId);
}