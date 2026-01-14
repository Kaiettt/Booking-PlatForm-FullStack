package com.booking.KBookin.repository.wishlist;

import com.booking.KBookin.entity.wishlist.Wishlist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Optional<Wishlist> findByUserId(Long userId);
}
