package com.booking.KBookin.dto.user;

import com.booking.KBookin.enumerate.user.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRoleRequestDTO implements Serializable {
    @NotNull
    private UserRole role;
}

