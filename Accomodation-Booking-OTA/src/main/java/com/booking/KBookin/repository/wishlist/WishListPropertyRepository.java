package com.booking.KBookin.repository.wishlist;

import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.wishlist.Wishlist;
import com.booking.KBookin.entity.wishlist.WishlistProperty;
import com.booking.KBookin.repository.projection.wishlist.WishlistPropertiesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListPropertyRepository extends JpaRepository<WishlistProperty,Long> {
    boolean existsByWishlistAndProperty(Wishlist wishlist, Property property);
    @Query("""
        select
            wp.id as id,
            w.id as wishlistId,
            p.id as propertyId
        from WishlistProperty wp
        join wp.wishlist w
        join wp.property p
        where w.user.id = :userId
    """)
    List<WishlistPropertiesProjection> findAllByUserId(Long userId);

}
