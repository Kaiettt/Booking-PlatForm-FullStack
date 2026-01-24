package com.booking.KBookin.service.property;

import com.booking.KBookin.dto.property.PropertyCreateItemRequest;
import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.property.PropertyResponseDTO;
import jakarta.validation.Valid;

import java.util.UUID;

public interface PropertyService {
    Long createProperty(PropertyCreateRequest propertyCreateRequest);

    Long updatePropertyItems(Long propertyId, @Valid PropertyCreateItemRequest request);
}
