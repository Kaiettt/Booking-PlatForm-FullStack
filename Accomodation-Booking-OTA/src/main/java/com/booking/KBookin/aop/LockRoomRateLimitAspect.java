package com.booking.KBookin.aop;

import com.booking.KBookin.security.LockRoomRateLimitService;
import com.booking.KBookin.security.SearchRateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

import static com.booking.KBookin.config.Common.AUTHENTICATED_MAX_ALLOWED;
import static com.booking.KBookin.config.Common.GUEST_MAX_ALLOWED;
import static com.booking.KBookin.util.SecurityUtil.getUserIdIfAuthenticated;

@Aspect
@Component
@AllArgsConstructor
public class LockRoomRateLimitAspect {

    private final LockRoomRateLimitService lockRoomRateLimitService;
    private final HttpServletRequest request;
    @Before("@annotation(SearchRateLimit)")
    public void applySearchRateLimit() throws AuthenticationException {
        String userId = getUserIdIfAuthenticated();
        if (userId != null) {
            String key = "rate:search:user:" + userId;
            lockRoomRateLimitService.checkLimit(key);
        } else {
            throw new AuthenticationException(
                    "Please sign in to continue with your reservation."
            );
        }
    }
}
