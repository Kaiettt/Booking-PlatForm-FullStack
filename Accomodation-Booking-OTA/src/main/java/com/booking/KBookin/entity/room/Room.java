// src/main/java/com/booking/KBookin/entity/room/Room.java
package com.booking.KBookin.entity.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.booking.CheckIn;
import com.booking.KBookin.enumerate.property.RoomStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false)
    @Builder.Default
    private RoomStatus roomStatus = RoomStatus.AVAILABLE;

    @Column(name = "last_cleaned_at")
    @Builder.Default
    private LocalDateTime lastCleanedAt = LocalDateTime.now();

    /**
     * Optional: Ensures that the date is set specifically when
     * the entity is first saved to the database.
     */
    @PrePersist
    protected void onCreate() {
        if (this.lastCleanedAt == null) {
            this.lastCleanedAt = LocalDateTime.now();
        }
        if (this.roomStatus == null) {
            this.roomStatus = RoomStatus.AVAILABLE;
        }
    }

}