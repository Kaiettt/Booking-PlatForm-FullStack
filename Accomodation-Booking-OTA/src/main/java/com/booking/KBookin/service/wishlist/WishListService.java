package com.booking.KBookin.service.wishlist;

import com.booking.KBookin.dto.wishlist.WishListRequestDTO;
import com.booking.KBookin.dto.wishlist.WishListResponseDTO;
import jakarta.validation.Valid;

public interface WishListService {
    WishListResponseDTO addToWishList(@Valid WishListRequestDTO wishListService);

    WishListResponseDTO fetchAllByUser(Long userId);
}
