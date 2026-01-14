package com.booking.KBookin.dto.booking;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingGuestResponse implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String nationality;
}