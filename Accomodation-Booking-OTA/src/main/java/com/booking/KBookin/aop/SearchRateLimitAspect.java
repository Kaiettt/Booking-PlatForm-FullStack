package com.booking.KBookin.aop;

import com.booking.KBookin.config.Common;
import com.booking.KBookin.security.SearchRateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.booking.KBookin.config.Common.AUTHENTICATED_MAX_ALLOWED;
import static com.booking.KBookin.config.Common.GUEST_MAX_ALLOWED;
import static com.booking.KBookin.util.SecurityUtil.getUserIdIfAuthenticated;

@Aspect
@Component
@AllArgsConstructor
public class SearchRateLimitAspect {
    private final SearchRateLimitService searchRateLimitService;
    private final HttpServletRequest request;
    @Before("@annotation(SearchRateLimit)")
    public void applySearchRateLimit() {
        String ip = request.getRemoteAddr();
        String userId = getUserIdIfAuthenticated();
        if (userId != null) {
            String key = "rate:search:user:" + userId;
            searchRateLimitService.checkLimit(key, AUTHENTICATED_MAX_ALLOWED);
        } else {
            String key = "rate:search:ip:" + ip;
            searchRateLimitService.checkLimit(key,GUEST_MAX_ALLOWED );
        }
    }

}
