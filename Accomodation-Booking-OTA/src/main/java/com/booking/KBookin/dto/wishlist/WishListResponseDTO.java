package com.booking.KBookin.dto.wishlist;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class WishListResponseDTO implements Serializable {
    private Long id;
    private Long userId;
    @Builder.Default
    private List<WishlistPropertiesResponse> wishlistProperties = new ArrayList<>();
}
