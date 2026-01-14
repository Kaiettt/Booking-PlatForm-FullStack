package com.booking.KBookin.dto.property;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PropertyAmenityDTO implements Serializable {
    private  Long id;
    private  String name;
}
