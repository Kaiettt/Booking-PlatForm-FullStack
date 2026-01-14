package com.booking.KBookin.entity.rate;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.property.CancellationPolicy;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.enumerate.rate.PrepaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rate_plans")
public class RatePlan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Column(nullable = false)
    private String name;
    
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_policy_id")
    private CancellationPolicy cancellationPolicy;

    @Enumerated(EnumType.STRING)
    private PrepaymentType prepaymentType;

    @ManyToMany
    @JoinTable(
        name = "rate_plan_perks",
        joinColumns = @JoinColumn(name = "rate_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "perk_id")
    )
    @Builder.Default
    private Set<Perk> perks = new HashSet<>();

    // Price is now directly stored in the RatePlan entity
}
