package com.booking.KBookin.entity.inventory;

import java.time.LocalDate;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.room.RoomType;

import com.booking.KBookin.exception.BusinessProcessException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "room_inventory")
public class RoomInventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    private LocalDate date;
    private Integer availableRooms;

    public void decreaseAvailability(int quantity) {

        int available = this.getAvailableRooms();
        if (available < quantity) {
            throw new BusinessProcessException(
                    String.format(
                            "Not enough rooms on %s for roomTypeId %d",
                            this.getDate(),
                            this.getRoomType().getId()
                    )
            );
        }

        this.setAvailableRooms(available - quantity);
    }
}
