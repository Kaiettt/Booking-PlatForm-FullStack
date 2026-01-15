package com.booking.KBookin.dto.wishlist;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class WishlistPropertiesResponse implements Serializable {
    private Long id;
    private Long wishListId;
    private Long propertyId;
}
