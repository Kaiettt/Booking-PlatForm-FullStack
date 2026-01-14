package com.booking.KBookin.controller.auth;


import com.booking.KBookin.config.ApiMessage;
import com.booking.KBookin.config.Common;
import com.booking.KBookin.dto.auth.LoginDTO;
import com.booking.KBookin.dto.auth.LoginResponce;
import com.booking.KBookin.service.auth.AuthenicationService;
import com.booking.KBookin.service.user.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenicationService authenicationService;
    private final UserService userService;

    public AuthenticationController( UserService userService,AuthenicationService authenicationService,AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenicationService = authenicationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<LoginResponce> login(@Valid @RequestBody LoginDTO loginDto)  {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponce loginResponce = this.authenicationService.handleAuthentication(authentication,loginDto.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,this.authenicationService.getCookie(loginResponce.getRefreshToken()).toString()).body(loginResponce);
    }



    @GetMapping("/refresh")
    @ApiMessage("Get Access Token")
    public ResponseEntity<LoginResponce> getAccessToken(@CookieValue(name = "refresh-token", defaultValue = "") String refresh_token) throws EntityExistsException {
        if(refresh_token == null || refresh_token.isEmpty()) {
            throw new EntityExistsException(Common.REFRESH_TOKEN_NOT_FOUND);
        }
        LoginResponce loginResponce = this.authenicationService.getAccessToken(refresh_token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, this.authenicationService.getCookie(loginResponce.getRefreshToken()).toString())
                .body(loginResponce);
    }

    @PostMapping("/google")
    @ApiMessage("Login with Google successfully")
    public ResponseEntity<LoginResponce> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("token");
        try {
            LoginResponce response = this.authenicationService.handleGoogleLogin(idTokenString);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, this.authenicationService.getCookie(response.getRefreshToken()).toString())
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/tokens")
    public ResponseEntity<Map<String, Object>> createEmailVerifyToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        this.authenicationService.createEmailTokenverification(email);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Verification email sent successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirm(@RequestBody Map<String, String> request) {
        long token =  Long.parseLong(request.get("token"));
        String email = request.get("email");

        this.authenicationService.handleEmailConfirmation(token,email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verified successfully.");
        return ResponseEntity.ok(response);
    }
}