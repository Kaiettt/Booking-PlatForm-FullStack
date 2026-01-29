package com.booking.KBookin.dto.property;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCreateItemRequest implements Serializable {
    @NotNull(message = "Property Id type is required")
    private Long propertyId;
    private List<Long> amenityIds;
    private List<Long> facilityIds;
    private List<MultipartFile> images;
}
