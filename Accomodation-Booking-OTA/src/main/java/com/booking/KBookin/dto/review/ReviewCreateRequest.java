package com.booking.KBookin.dto.review;
import jakarta.validation.constraints.*;

import java.io.Serializable;

public record ReviewCreateRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId,
        @NotNull(message = "Property ID cannot be null")
        Long propertyId,
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        @NotNull
        Integer rating,
        @Size(max = 500, message = "Comment is too long")
        String comment
) implements Serializable {
}
