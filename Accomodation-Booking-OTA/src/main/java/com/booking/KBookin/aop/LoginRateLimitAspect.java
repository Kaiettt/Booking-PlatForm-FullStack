package com.booking.KBookin.aop;

import com.booking.KBookin.security.LoginRateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@Aspect
@Component
public class LoginRateLimitAspect {

    private final LoginRateLimitService loginRateLimitService;
    private final HttpServletRequest request;

    @Before("@annotation(LoginRateLimit)")
    public void beforeLogin() {
        String ip = getClientIp();
        loginRateLimitService.checkRateLimit(ip);
    }

    @AfterThrowing(
            pointcut = "@annotation(LoginRateLimit)",
            throwing = "ex"
    )
    public void onLoginFailure(Exception ex) {
        if (ex instanceof BadCredentialsException) {
            String ip = getClientIp();
            loginRateLimitService.loginFailed(ip);
        }
    }

    private String getClientIp() {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}

