package com.booking.KBookin.mapper.review;

import com.booking.KBookin.dto.review.ReviewCreateRequest;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.entity.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "id", ignore = true)
    Review toEntity(ReviewCreateRequest request);

    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "property.id", target = "propertyId")
    ReviewResponseDTO toResponseDTO(Review review);
}