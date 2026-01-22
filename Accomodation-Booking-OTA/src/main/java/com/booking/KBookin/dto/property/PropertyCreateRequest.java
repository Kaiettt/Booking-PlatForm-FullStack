package com.booking.KBookin.dto.property;

import com.booking.KBookin.dto.location.AddressCreateDTO;
import com.booking.KBookin.enumerate.property.PropertyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PropertyCreateRequest implements Serializable {
    @NotBlank(message = "Property name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description should be at least 20 characters long")
    private String description;

    @NotNull(message = "Property type is required")
    private PropertyType type;

    @Valid // Critical: Triggers validation on the nested Address object
    @NotNull(message = "Address information is required")
    private AddressCreateDTO address;

    @NotNull(message = "User id is required")
    private Long userId;
}