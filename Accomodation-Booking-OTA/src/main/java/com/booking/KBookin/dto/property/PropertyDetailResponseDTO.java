package com.booking.KBookin.dto.property;

import com.booking.KBookin.dto.media.MediaDTO;
import com.booking.KBookin.dto.review.ReviewResponseDTO;
import com.booking.KBookin.dto.room.RoomTypeDTO;
import com.booking.KBookin.entity.location.Address;
import lombok.*;

import java.util.List;

// Sử dụng @Data để có sẵn Getter, Setter, toString, equals/hashCode.
// Sử dụng @AllArgsConstructor để có constructor với tất cả các trường.
// Sử dụng @NoArgsConstructor để có constructor không tham số (thường cần cho framework như Jackson).
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PropertyDetailResponseDTO {

    private Long id;
    private String name;
    private String type;
    private String description;
    private Address address;
    private String status;
    private Double avgRating;
    private Integer totalRating;

    // Thêm trường minPrice nếu cần, đây là lợi thế khi dùng class
    private Double minPrice;

    private List<MediaDTO> media;
    private List<PropertyAmenityDTO> amenities;
    private List<PropertyFacilityDTO> facilities;
    private List<RoomTypeDTO> roomTypes;
    private List<ReviewResponseDTO> reviews;

}