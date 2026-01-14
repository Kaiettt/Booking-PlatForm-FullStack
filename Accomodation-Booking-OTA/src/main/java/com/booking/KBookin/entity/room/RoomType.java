// src/main/java/com/booking/KBookin/entity/room/RoomType.java
package com.booking.KBookin.entity.room;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.inventory.RoomInventory;
import com.booking.KBookin.entity.media.RoomTypeMedia;
import com.booking.KBookin.entity.property.CancellationPolicy;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.room.RoomAmenity;
import com.booking.KBookin.entity.room.RoomFacility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "room_types")
public class RoomType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    @Column(nullable = false)
    private String name;

    @Column(name = "max_adults")
    private Integer maxAdults;

    @Column(name = "max_children")
    private Integer maxChildren;

    @Column(name = "max_guest")
    private Integer maxGuest;

    @Column(name = "size_m2")
    private Integer sizeM2;

    private String bedType;

    private String viewType;

    private Boolean smokingAllowed;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "room_types_amenities",
        joinColumns = @JoinColumn(name = "room_type_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @BatchSize(size = 20)
    @Builder.Default
    private Set<RoomAmenity> amenities = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "room_types_facilities",
        joinColumns = @JoinColumn(name = "room_type_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    @BatchSize(size = 20)
    @Builder.Default
    private Set<RoomFacility> facilities = new HashSet<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<RoomTypeMedia> media = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoomInventory> roomInventories = new ArrayList<>();

    @Column(name = "total_rooms", nullable = false)
    private Integer totalRooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private Set<RatePlan> ratePlans = new HashSet<>(); // <-- Changed List to Set
}