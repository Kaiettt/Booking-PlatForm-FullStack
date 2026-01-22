package com.booking.KBookin.service.property;

import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.property.PropertyResponseDTO;

import java.util.UUID;

public interface PropertyService {
    Long createProperty(PropertyCreateRequest propertyCreateRequest);
}
