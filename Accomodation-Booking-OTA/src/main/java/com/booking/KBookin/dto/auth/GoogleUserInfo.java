package com.booking.KBookin.dto.auth;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleUserInfo {
      private String email;
    private String name;
    private boolean emailVerified;
}
