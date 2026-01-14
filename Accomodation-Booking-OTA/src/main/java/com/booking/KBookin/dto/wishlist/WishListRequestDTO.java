package com.booking.KBookin.dto.wishlist;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class WishListRequestDTO implements Serializable {

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Property ID must not be null")
    @Positive(message = "Property ID must be a positive number")
    private Long propertyId;
}
