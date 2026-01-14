package com.booking.KBookin.service.auth;

import com.booking.KBookin.entity.auth.ConfirmationToken;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.auth.AuthProvider;
import com.booking.KBookin.enumerate.user.UserRole;
import com.booking.KBookin.enumerate.user.UserStatus;
import com.booking.KBookin.exception.VerificationException;
import com.booking.KBookin.service.notification.EmailService;
import com.booking.KBookin.service.user.UserService;
import com.booking.KBookin.util.SecurityUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.booking.KBookin.config.Common;
import com.booking.KBookin.dto.auth.GoogleUserInfo;
import com.booking.KBookin.dto.auth.LoginResponce;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@RequiredArgsConstructor
@Service
public class AuthenicationService {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final EmailService emailService;
    private final ConfirmationService confirmationService;
    private final OauthVerifier oAuthVerifier;
    public LoginResponce getAccessToken(String refreshToken) throws EntityExistsException {
        User user = this.userService.getUserByRefreshToken(refreshToken);
        User userLogin = User.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isPhoneVerified(user.getIsPhoneVerified())
                .isEmailVerified(user.getIsEmailVerified())
                .status(user.getStatus())
                .build();

        String accessToken = this.securityUtil.createAccessTokenToken(user.getEmail(),user);
        String newRefreshToken = this.securityUtil.createRefreshToken(user.getEmail(), user);
        user.setRefreshToken(newRefreshToken);
        this.userService.updateUser(user);


        return LoginResponce.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .user(userLogin)

                .build();
    }

    public LoginResponce handleAuthentication(Authentication authentication, String username) {
        User user = this.userService.getUserByUserName(username);

        User userLogin = User.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isPhoneVerified(user.getIsPhoneVerified())
                .isEmailVerified(user.getIsEmailVerified())
                .status(user.getStatus())
                .build();

        String accessToken = this.securityUtil.createAccessTokenToken(username, user);
        String refreshToken = this.securityUtil.createRefreshToken(username, user);

        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);
        return LoginResponce.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userLogin)
                .build();
    }


    public ResponseCookie getCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.securityUtil.jwtRefreshTokenExpiration)
                .build();
    }

    public LoginResponce handleGoogleLogin(String idTokenString) {
            GoogleUserInfo info = oAuthVerifier.verify(idTokenString);
            String email = info.getEmail();

            User user = userService.getUserByEmail(email);

            boolean isUserNoExist = false;
            // 2) Nếu user chưa tồn tại -> tạo mới
            if (user == null) {
                isUserNoExist = true;
                user = new User();
                user.setEmail(email);
                user.setRole(UserRole.GUEST);
                user.setStatus(UserStatus.ACTIVE);
                user.setIsEmailVerified(info.isEmailVerified());
                user.setFullName(info.getName());
                user.setPasswordHash(Common.OAUTH_PROVIDER_PASSWORD);
                user.setIsPhoneVerified(false);
                user.setProvider(AuthProvider.GOOGLE);
            }

            // 3) Sinh access + refresh token giống flow bình thường
            String accessToken = this.securityUtil.createAccessTokenToken(email, user);
            String refreshToken = this.securityUtil.createRefreshToken(email, user);
            user.setRefreshToken(refreshToken);
            if(isUserNoExist){
                user = this.userService.saveUser(user);
            }

            // 5) Tạo user object trả về (tuỳ theo LoginResponce bạn dùng)
            User userLogin = User.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .phoneNumber(user.getPhoneNumber())
                    .isPhoneVerified(user.getIsPhoneVerified())
                    .isEmailVerified(user.getIsEmailVerified())
                    .status(user.getStatus())
                    .build();
            return LoginResponce.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(userLogin)
                    .build();
    }

    public void createEmailTokenverification(String email) {
        User user = this.userService.getUserByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        long verificationToken = this.confirmationService.createNewEmailVerficationToken(user);
        this.emailService.sendToken(email, user.getFullName(),verificationToken);

    }


    @Transactional
    public void handleEmailConfirmation(long token,String email) {
        ConfirmationToken confirmationToken = this.confirmationService.getConfirmationByTokenAndEmail(token,email);
        if(confirmationToken.getConfirmedAt() != null){
            throw new VerificationException("email is already confirmed");
        }
        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new VerificationException("email is already expired");
        }
        this.confirmationService.confirmToken(token,email);
        User user = this.userService.getUserById(confirmationToken.getUser().getId());
        user.setIsEmailVerified(true);
        this.userService.saveUser(user);

    }
}
