package com.booking.KBookin.mapper.wishlist;

import com.booking.KBookin.dto.wishlist.WishListResponseDTO;
import com.booking.KBookin.dto.wishlist.WishlistPropertiesResponse;
import com.booking.KBookin.entity.wishlist.Wishlist;
import com.booking.KBookin.entity.wishlist.WishlistProperty;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "wishlistProperties", target = "wishlistProperties")
    WishListResponseDTO toResponse(Wishlist wishlist);

    List<WishlistPropertiesResponse> toWishlistPropertiesResponse(
            List<WishlistProperty> wishlistProperties);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "wishlist.id", target = "wishListId")
    @Mapping(source = "property.id", target = "propertyId")
    WishlistPropertiesResponse toWishlistPropertyResponse(
            WishlistProperty wishlistProperty);
}
