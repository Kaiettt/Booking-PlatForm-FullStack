package com.booking.KBookin.dto.property;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class HostInfo implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
}
