package com.booking.KBookin.mapper;

import com.booking.KBookin.dto.location.AddressCreateDTO;
import com.booking.KBookin.dto.location.GeoLocationCreateDTO;
import com.booking.KBookin.entity.location.Address;
import com.booking.KBookin.entity.location.GeoLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    // MapStruct handles lat/lng automatically as names match
    GeoLocation toGeoEntity(GeoLocationCreateDTO dto);

    // MapStruct handles nested 'geo' automatically as names match
    // We ignore fullAddress in the mapping because the entity's getFullAddress()
    // logic handles the concatenation.
    @Mapping(target = "fullAddress", ignore = true)
    Address toAddressEntity(AddressCreateDTO dto);

    // Reverse mapping for responses
    AddressCreateDTO toAddressDto(Address entity);
}