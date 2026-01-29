package com.booking.KBookin.repository.projection.room;

import com.booking.KBookin.dto.media.MediaDTO;
import java.util.List;

import com.booking.KBookin.dto.rate_plan.RatePlanResponse;
import org.springframework.beans.factory.annotation.Value;

public interface RoomTypeHostProjection {
    Long getId();
    String getName();
    Integer getMaxAdults();
    Integer getMaxChildren();
    Integer getMaxGuest();
    Integer getSizeM2();
    String getBedType();
    String getViewType();
    Boolean getSmokingAllowed();
    Integer getTotalRooms();

    // Map String Collections (Amenities/Facilities)
    @Value("#{target.amenities.![name]}")
    List<String> getRoomAmenities();

    @Value("#{target.facilities.![name]}")
    List<String> getRoomFacilities();

    // Map MediaDTO
    @Value("#{target.media.![new com.booking.KBookin.dto.media.MediaDTO(id, url)]}")
    List<MediaDTO> getMedia();

    // Deep Mapping: RatePlans -> Perks
    @Value("#{target.ratePlans.![new com.booking.KBookin.dto.rate_plan.RatePlanResponse(" +
            "id, name, price, prepaymentType, " +
            "perks.![new com.booking.KBookin.dto.rate_plan.PerkResponse(id, code, name)])]}")
    List<RatePlanResponse> getRatePlans();
}