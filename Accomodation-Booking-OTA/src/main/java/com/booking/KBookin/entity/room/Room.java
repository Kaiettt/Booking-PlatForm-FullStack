// src/main/java/com/booking/KBookin/entity/room/Room.java
package com.booking.KBookin.entity.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.booking.CheckIn;
import com.booking.KBookin.enumerate.property.RoomStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @Column(name = "room_status")
    private RoomStatus roomStatus;

    @Column(name = "last_cleaned_at")
    private LocalDateTime lastCleanedAt;

}