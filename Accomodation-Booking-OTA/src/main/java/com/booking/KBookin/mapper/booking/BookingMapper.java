package com.booking.KBookin.mapper.booking;

import com.booking.KBookin.dto.booking.*;
import com.booking.KBookin.dto.rate_plan.PerkResponse;
import com.booking.KBookin.dto.rate_plan.RatePlanResponse;
import com.booking.KBookin.entity.booking.*;
        import com.booking.KBookin.entity.rate.RatePlan;
import com.booking.KBookin.entity.rate.Perk;
import com.booking.KBookin.repository.projection.booking.BookingProjection;
import org.mapstruct.*;

        import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bookingItems", target = "bookingItems")
    BookingResponse toResponse(Booking booking);


    @Mapping(source = "roomType.id", target = "roomTypeId")
    @Mapping(source = "roomType.name", target = "roomTypeName")
    BookingItemResponse toItemResponse(BookingItem bookingItem);

    List<BookingItemResponse> toItemResponseList(List<BookingItem> bookingItems);

    BookingGuestResponse toGuestResponse(BookingGuest guest);


    @Mapping(source = "prepaymentType", target = "prepaymentType")
    RatePlanResponse toRatePlanResponse(RatePlan ratePlan);


    PerkResponse toPerkResponse(Perk perk);

    List<PerkResponse> toPerkResponseList(Set<Perk> perks);

    BookingResponse toResponseFromProjection(BookingProjection bookingProjection);

    List<BookingItemResponse> toItemResponseListFromProjection(List<BookingProjection.BookingItemProjection> bookingItems);
}
