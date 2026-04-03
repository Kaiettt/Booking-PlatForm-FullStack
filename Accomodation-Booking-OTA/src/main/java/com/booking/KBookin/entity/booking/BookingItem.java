// src/main/java/com/booking/KBookin/entity/booking/BookingItem.java
package com.booking.KBookin.entity.booking;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.room.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "booking_items")
public class BookingItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_plan_id",nullable = true)
    private RatePlan ratePlan;


    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    public void calculateAmount() {
        this.amount = BigDecimal.valueOf(this.quantity * this.ratePlan.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookingItem that = (BookingItem) o;
        return Objects.equals(id, that.id) && Objects.equals(booking, that.booking) && Objects.equals(roomType, that.roomType) && Objects.equals(ratePlan, that.ratePlan) && Objects.equals(quantity, that.quantity) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, booking, roomType, ratePlan, quantity, amount);
    }
}