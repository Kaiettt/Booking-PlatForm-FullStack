package com.booking.KBookin.dto.auth;
import com.booking.KBookin.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponce {
    private String accessToken;
    @JsonIgnore
    private String refreshToken;
    private User user;
    private Set<Long> wishlist;
}
