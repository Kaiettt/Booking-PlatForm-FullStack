package com.booking.KBookin.service.property.impl;

import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.property.PropertyResponseDTO;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.mapper.property.PropertyMapper;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.user.UserRepository;
import com.booking.KBookin.service.property.PropertyService;
import com.booking.KBookin.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyMapper propertyMapper;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    @Override
    public Long createProperty(PropertyCreateRequest propertyCreateRequest) {
        User host = this.userRepository.findById(propertyCreateRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Host not found"));
        Property property = this.propertyMapper.fromCreateDtoToEntity(propertyCreateRequest,host);
        property.handleCreateProperty();
        Property savedProperty = this.propertyRepository.save(property);
        return savedProperty.getId();
    }
}
