package com.booking.KBookin.dto.room;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.rate_plan.RatePlanResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RoomTypeDTO implements Serializable {
    private Long id;
    private  String name;
    private  Integer maxAdults;
    private   Integer maxChildren;
    private   Integer maxGuest;
    private   Integer sizeM2;
    private   String bedType;
    private   String viewType;
    private   Boolean smokingAllowed;
    private   Integer totalRooms;
    private   List<String> roomAmenities;
    private   List<String> roomFacilities;
    private   List<MediaDTO> media;
    private   List<RatePlanResponse> ratePlans;
}
