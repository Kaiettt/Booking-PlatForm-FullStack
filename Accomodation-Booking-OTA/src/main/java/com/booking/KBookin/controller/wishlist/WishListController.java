package com.booking.KBookin.controller.wishlist;

import com.booking.KBookin.dto.wishlist.WishListRequestDTO;
import com.booking.KBookin.dto.wishlist.WishListResponseDTO;
import com.booking.KBookin.service.wishlist.WishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
public class WishListController {
    private final WishListService wishListService;

    @PostMapping
    public ResponseEntity<WishListResponseDTO> createWishList(@Valid @RequestBody WishListRequestDTO wishListRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.wishListService.addToWishList(wishListRequestDTO));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<WishListResponseDTO> fetchAll(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.wishListService.fetchAllByUser(userId));
    }
}
