package com.booking.KBookin.mapper.property;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.property.PropertyAmenityDTO;
import com.booking.KBookin.dto.property.PropertyCreateRequest;
import com.booking.KBookin.dto.property.PropertyDetailResponseDTO;
import com.booking.KBookin.dto.property.PropertyFacilityDTO;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.dto.search.SearchResponseDTO;
import com.booking.KBookin.entity.media.Media;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.property.PropertyAmenity;
import com.booking.KBookin.entity.property.PropertyFacility;
import com.booking.KBookin.entity.review.Review;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.mapper.location.LocationMapper;
import com.booking.KBookin.mapper.room.RoomTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class, LocationMapper.class})
public interface PropertyMapper {

    // Ánh xạ chi tiết Property (searchRoomTypeWithFilter)
    // SỬA LỖI: Sử dụng "enumToString" từ RoomTypeMapper để ánh xạ Enum
    @Mapping(source = "type", target = "type", qualifiedByName = "enumToString")
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    PropertyDetailResponseDTO toDetailDto(Property property);

    // Ánh xạ các collection
    Set<MediaDTO> toMediaDTOs(Set<Media> media);
    Set<PropertyAmenityDTO> toAmenityDTOs(Set<PropertyAmenity> amenities);
    Set<PropertyFacilityDTO> toFacilityDTOs(Set<PropertyFacility> facilities);
    Set<ReviewResponseDTO> toReviewDTOs(Set<Review> reviews);

    // Ánh xạ review cần custom logic cho userName (user.getFullName())
    @Mapping(source = "user", target = "userName", qualifiedByName = "mapUserName")
    @Mapping(source = "property.id", target = "propertyId")
    ReviewResponseDTO toReviewDTO(Review review);

    // Custom logic để handle null cho rv.getUser()
    @Named("mapUserName")
    default String mapUserName(com.booking.KBookin.entity.user.User user) {
        return Optional.ofNullable(user)
                .map(com.booking.KBookin.entity.user.User::getFullName)
                .orElse("Anonymous");
    }

    // Ánh xạ cho kết quả tìm kiếm (toResponseDTO)
    // SỬA LỖI: Sử dụng "enumToString" từ RoomTypeMapper để ánh xạ Enum
    @Mapping(source = "type", target = "type", qualifiedByName = "enumToString")
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    @Mapping(source = "media", target = "mediaUrl", qualifiedByName = "mapFirstMediaUrl")
    SearchResponseDTO toSearchDto(Property property);

    // Custom logic để lấy URL của phần tử đầu tiên
    @Named("mapFirstMediaUrl")
    default String mapFirstMediaUrl(Set<Media> mediaSet) {
        return Optional.ofNullable(mediaSet)
                .orElseGet(Collections::emptySet)
                .stream()
                .findFirst()
                .map(Media::getUrl)
                .orElse(null);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "DRAFT") // Default status
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "media", ignore = true)
// Explicitly map the host parameter to the property's host field
    @Mapping(source = "host", target = "host")
// Delegate the nested DTO mapping to LocationMapper
    @Mapping(source = "propertyCreateRequest.address", target = "address")
    Property fromCreateDtoToEntity(PropertyCreateRequest propertyCreateRequest, User host);
}