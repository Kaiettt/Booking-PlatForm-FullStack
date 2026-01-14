// src/main/java/com/booking/KBookin/entity/wishlist/WishlistProperty.java
package com.booking.KBookin.entity.wishlist;

import com.booking.KBookin.entity.BaseEntity;
import com.booking.KBookin.entity.property.Property;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wishlist_properties")
public class WishlistProperty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}