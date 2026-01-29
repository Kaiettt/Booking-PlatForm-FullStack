package com.booking.KBookin.repository.projection.property;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.property.HostInfo;
import com.booking.KBookin.dto.property.PropertyAmenityDTO;
import com.booking.KBookin.dto.property.PropertyFacilityDTO;
import com.booking.KBookin.entity.location.Address;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public interface PropertyHostProjection {
    Long getId();
    String getName();
    String getType();
    String getDescription();
    String getStatus();
    Double getAvgRating();
    Integer getTotalRating();
    Address getAddress();

    @Value("#{new com.booking.KBookin.dto.property.HostInfo(target.host.id, target.host.fullName, target.host.email, target.host.phoneNumber)}")
    HostInfo getHost();

    // Fix: Using SpEL Projection syntax ![] instead of .stream().map()
    @Value("#{target.media.![new com.booking.KBookin.dto.media.MediaDTO(id, url)]}")
    List<MediaDTO> getMedia();

    @Value("#{target.amenities.![new com.booking.KBookin.dto.property.PropertyAmenityDTO(id, name)]}")
    List<PropertyAmenityDTO> getAmenities();

    @Value("#{target.facilities.![new com.booking.KBookin.dto.property.PropertyFacilityDTO(id, name)]}")
    List<PropertyFacilityDTO> getFacilities();
}