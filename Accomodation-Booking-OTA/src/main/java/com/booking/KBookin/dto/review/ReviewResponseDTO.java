package com.booking.KBookin.dto.review;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ReviewResponseDTO implements Serializable {
    private Long id;
    private String userName;
    private Long propertyId;
    private Integer rating;
    private String comment;
}