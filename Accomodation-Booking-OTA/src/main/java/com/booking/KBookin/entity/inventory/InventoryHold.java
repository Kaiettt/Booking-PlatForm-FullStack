package com.booking.KBookin.entity.inventory;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.booking.CanceledBy;
import com.booking.KBookin.enumerate.booking.InventoryHoldStatus;
import com.booking.KBookin.exception.BusinessProcessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.booking.KBookin.config.Common.EXTEND_LOCK_TIME_MINUTES;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory_holds")
public class InventoryHold extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryHoldStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(
            mappedBy = "inventoryHold",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InventoryHoldDetail> inventoryHoldDetails = new ArrayList<>();

    // Convenience methods (IMPORTANT)
    public void addDetail(InventoryHoldDetail detail) {
        inventoryHoldDetails.add(detail);
        detail.setInventoryHold(this);
    }

    public void removeDetail(InventoryHoldDetail detail) {
        inventoryHoldDetails.remove(detail);
        detail.setInventoryHold(null);
    }

    public void extendLock() {
        if(this.expiresAt.isBefore(LocalDateTime.now())){
            throw new BusinessProcessException("Cannot extend expired room lock");
        }
        this.status = InventoryHoldStatus.HARD_LOCK;
        this.expiresAt = this.expiresAt.plusMinutes(EXTEND_LOCK_TIME_MINUTES);
    }
}
