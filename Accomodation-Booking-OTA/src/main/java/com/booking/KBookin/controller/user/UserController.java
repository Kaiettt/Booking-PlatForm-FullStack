package com.booking.KBookin.controller.user;

import com.booking.KBookin.dto.user.AdminUserResponseDTO;
import com.booking.KBookin.dto.user.UpdateUserRoleRequestDTO;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.auth.AuthProvider;
import com.booking.KBookin.enumerate.user.UserRole;
import com.booking.KBookin.repository.user.UserRepository;
import com.booking.KBookin.enumerate.user.UserStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AdminUserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserResponseDTO> result = users.stream()
                .map(this::toAdminUserResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<AdminUserResponseDTO> updateUserRole(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserRoleRequestDTO request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

        user.setRole(request.getRole());
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(toAdminUserResponse(saved));
    }

    private AdminUserResponseDTO toAdminUserResponse(User user) {
        return AdminUserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .isEmailVerified(user.getIsEmailVerified())
                .isPhoneVerified(user.getIsPhoneVerified())
                .provider(user.getProvider())
                .build();
    }
}
