// src/main/java/com/booking/KBookin/entity/property/Property.java
package com.booking.KBookin.entity.property;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.media.Media;
import com.booking.KBookin.entity.review.Review;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.entity.location.Address;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.property.PropertyStatus;
import com.booking.KBookin.enumerate.property.PropertyType;
import com.booking.KBookin.repository.property.impl.PropertySearchResultImpl;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SqlResultSetMapping(
        name = "PropertySearchResultMapping",
        classes = @ConstructorResult(
                targetClass = PropertySearchResultImpl.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "minPrice", type = java.math.BigDecimal.class)
                }
        )
)
@Entity
@Table(name = "properties")
public class Property extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private PropertyType type;
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "host_id",
            nullable = false,
            columnDefinition = "bigint default 1"
    )
    private User host;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    private List<RoomType> roomTypes = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinTable(
        name = "property_amenity_mappings",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @Builder.Default
    private Set<PropertyAmenity> amenities = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinTable(
        name = "property_facility_mappings",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    @Builder.Default
    private Set<PropertyFacility> facilities = new HashSet<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Media> media = new HashSet<>(); // <-- Changed List to Set
    @Column(name = "avg_rating",nullable = true)
    private Double avgRating;
    @Column(name = "total_rating",nullable = true)
    private Integer totalRating;

    public void recalculateRating(Integer newRating) {
        if (this.avgRating == null) this.avgRating = 0.0;
        if (this.totalRating == null) this.totalRating = 0;

        Double newTotalScore = (this.avgRating * this.totalRating) + newRating;
        this.totalRating += 1;
        this.avgRating = newTotalScore / this.totalRating;
    }

    public void handleCreateProperty() {
        this.status = PropertyStatus.DRAFT;
        if (this.avgRating == null) this.avgRating = 0.0;
        if (this.totalRating == null) this.totalRating = 0;
    }
}