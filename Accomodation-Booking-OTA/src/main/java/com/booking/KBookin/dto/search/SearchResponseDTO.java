package com.booking.KBookin.dto.search;

import com.booking.KBookin.dto.property.PropertyAmenityDTO;
import com.booking.KBookin.dto.property.PropertyFacilityDTO;
import com.booking.KBookin.entity.location.Address;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SearchResponseDTO implements Serializable {
    private  Long id;
    private  String name;
    private  String type;
    private  String description;
    private  Address address;
    private  String status;
    private  String mediaUrl;
    private  BigDecimal minPrice;
    private  Double avgRating;
    private  Integer totalRating;
    private  List<PropertyAmenityDTO> amenities;
    private  List<PropertyFacilityDTO> facilities;
}