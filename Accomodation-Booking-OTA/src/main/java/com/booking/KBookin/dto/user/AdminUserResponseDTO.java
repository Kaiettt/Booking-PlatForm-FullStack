package com.booking.KBookin.dto.user;

import com.booking.KBookin.enumerate.auth.AuthProvider;
import com.booking.KBookin.enumerate.user.UserRole;
import com.booking.KBookin.enumerate.user.UserStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponseDTO implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private AuthProvider provider;
}

