package com.booking.KBookin.util;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.booking.KBookin.entity.user.User;


@Service
public class SecurityUtil {
    @Value("${jwt.access-token-validity-in-seconds}")
    public long jwtAccessTokenExpiration;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    public long jwtRefreshTokenExpiration;

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    public long getJwtRefreshTokenExpiration() {
        return jwtRefreshTokenExpiration;
    }

    public String createAccessTokenToken(String userName, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtAccessTokenExpiration, ChronoUnit.SECONDS);
        return createToken(userName, user, now, validity);
    }

    public String createRefreshToken(String userName, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtRefreshTokenExpiration, ChronoUnit.SECONDS);
        return createToken(userName, user, now, validity);
    }

    private String createToken(String userName, User user, Instant now, Instant validity) {
        List<String> authorities = new ArrayList<>();
        authorities.add(user.getRole().name());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userName)
                .claim("authorities", authorities)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}
