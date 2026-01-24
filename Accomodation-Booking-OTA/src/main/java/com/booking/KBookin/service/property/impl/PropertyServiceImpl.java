package com.booking.KBookin.service.property.impl;

import com.booking.KBookin.config.MinioBucketConfig;
import com.booking.KBookin.dto.property.PropertyCreateItemRequest;
import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.property.PropertyResponseDTO;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.property.PropertyAmenity;
import com.booking.KBookin.entity.property.PropertyFacility;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.mapper.property.PropertyMapper;
import com.booking.KBookin.repository.document.ComplianceDocumentRepository;
import com.booking.KBookin.repository.property.PropertyAmenityRepository;
import com.booking.KBookin.repository.property.PropertyFacilityRepository;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.user.UserRepository;
import com.booking.KBookin.service.media.MediaService;
import com.booking.KBookin.service.property.PropertyService;
import com.booking.KBookin.service.resource.ResourceService;
import com.booking.KBookin.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyMapper propertyMapper;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyAmenityRepository propertyAmenityRepository;
    private final PropertyFacilityRepository propertyFacilityRepository;
    private final MediaService mediaService;
    @Override
    public Long createProperty(PropertyCreateRequest propertyCreateRequest) {
        User host = this.userRepository.findById(propertyCreateRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Host not found"));
        Property property = this.propertyMapper.fromCreateDtoToEntity(propertyCreateRequest,host);
        property.handleCreateProperty();
        Property savedProperty = this.propertyRepository.save(property);
        return savedProperty.getId();
    }

    @Override
    public Long updatePropertyItems(Long propertyId, PropertyCreateItemRequest request) {
        Property property = this.propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property Not Found"));
        if(request.getAmenityIds() != null){
            List<PropertyAmenity> amenities = this.propertyAmenityRepository.findAllById(request.getAmenityIds());
            property.getAmenities().clear();
            property.getAmenities().addAll(new HashSet<>(amenities));
        }
        if (request.getFacilityIds() != null) {
            List<PropertyFacility> facilities = this.propertyFacilityRepository.findAllById(request.getFacilityIds());
            property.getFacilities().clear();
            property.getFacilities().addAll(new HashSet<>(facilities));
        }
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            this.mediaService.updatePropertyImages(request.getImages(),property);
        }
        property.handleCompleteRegister();
        return propertyRepository.save(property).getId();
    }
}
