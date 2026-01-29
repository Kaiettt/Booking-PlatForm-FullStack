package com.booking.KBookin.dto.property;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.dto.room.RoomTypeDTO;
import com.booking.KBookin.entity.location.Address;

import java.io.Serializable;
import java.util.List;

public class PropertyHostView implements Serializable {

    private Long id;
    private String name;
    private String type;
    private HostInfo host;
    private String description;
    private Address address;
    private String status;
    private Double avgRating;
    private Integer totalRating;
    private List<MediaDTO> media;
    private List<PropertyAmenityDTO> amenities;
    private List<PropertyFacilityDTO> facilities;
}
