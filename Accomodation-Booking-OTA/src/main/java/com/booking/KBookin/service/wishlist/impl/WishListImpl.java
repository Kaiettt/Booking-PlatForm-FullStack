package com.booking.KBookin.service.wishlist.impl;

import com.booking.KBookin.dto.wishlist.WishListRequestDTO;
import com.booking.KBookin.dto.wishlist.WishListResponseDTO;
import com.booking.KBookin.dto.wishlist.WishlistPropertiesResponse;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.entity.wishlist.Wishlist;
import com.booking.KBookin.entity.wishlist.WishlistProperty;
import com.booking.KBookin.exception.BusinessProcessException;
import com.booking.KBookin.mapper.wishlist.WishListMapper;
import com.booking.KBookin.repository.projection.wishlist.WishlistPropertiesProjection;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.user.UserRepository;
import com.booking.KBookin.repository.wishlist.WishListPropertyRepository;
import com.booking.KBookin.repository.wishlist.WishlistRepository;
import com.booking.KBookin.service.wishlist.WishListService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class WishListImpl implements WishListService {
    private final WishlistRepository wishlistRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final WishListPropertyRepository wishListPropertyRepository;
    @Override
    @CacheEvict(
            value = "wishlist",
            key = "#wishListRequestDTO.userId"
    )
    public WishListResponseDTO addToWishList(
            @Valid WishListRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Property not found"));

        Wishlist wishlist = wishlistRepository
                .findByUserId(dto.getUserId())
                .orElseGet(() ->
                        wishlistRepository.save(
                                Wishlist.builder()
                                        .user(user)
                                        .build()
                        )
                );

        if (wishListPropertyRepository
                .existsByWishlistAndProperty(wishlist, property)) {
            throw new BusinessProcessException(
                    "Property already in wishlist");
        }

        WishlistProperty savedItem =
                wishListPropertyRepository.save(
                        WishlistProperty.builder()
                                .wishlist(wishlist)
                                .property(property)
                                .build()
                );

        WishlistPropertiesResponse itemResponse =
                WishlistPropertiesResponse.builder()
                        .id(savedItem.getId())
                        .wishListId(wishlist.getId())
                        .propertyId(property.getId())
                        .build();

        return WishListResponseDTO.builder()
                .id(wishlist.getId())
                .userId(dto.getUserId())
                .wishlistProperties(List.of(itemResponse))
                .build();
    }


    @Override
    @Cacheable(
            value = "wishlist",
            key = "#userId"
    )
    public WishListResponseDTO fetchAllByUser(Long userId) {

        List<WishlistPropertiesProjection> rows =
                wishListPropertyRepository.findAllByUserId(userId);

        WishListResponseDTO response = new WishListResponseDTO();
        response.setUserId(userId);

        if (rows.isEmpty()) {
            return response;
        }

        response.setId(rows.get(0).getWishlistId());

        response.setWishlistProperties(
                rows.stream()
                        .map(r -> WishlistPropertiesResponse.builder()
                                .id(r.getId())
                                .wishListId(r.getWishlistId())
                                .propertyId(r.getPropertyId())
                                .build()
                        )
                        .toList()
        );

        return response;
    }

}
