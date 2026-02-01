package com.booking.KBookin.dto.room;

import com.booking.KBookin.enumerate.rate.PrepaymentType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RoomTypeFilterDTO {
    private String name;
    private Integer minGuest;
    private String bedType;
    private Boolean smokingAllowed;
    private List<String> amenities;
    private List<String> facilities;
    private Double maxPrice;
    private Set<PrepaymentType> prepaymentTypes;
}