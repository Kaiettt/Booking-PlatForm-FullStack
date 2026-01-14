// src/main/java/com/booking/KBookin/entity/booking/CheckIn.java
package com.booking.KBookin.entity.booking;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.enumerate.booking.CheckInStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "check_ins")
public class CheckIn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "checkin_room",
            joinColumns = @JoinColumn(name = "checkin_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms = new HashSet<>();

    @Column(name = "check_in_at", nullable = false)
    private LocalDateTime checkInAt;

    @Column(name = "check_out_at")
    private LocalDateTime checkOutAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckInStatus status;

    public void handleCheckout() {
        this.checkOutAt = LocalDateTime.now();
        this.status = CheckInStatus.CHECKED_OUT;
    }
}
