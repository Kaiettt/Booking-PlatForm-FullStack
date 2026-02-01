package com.booking.KBookin.security;

import com.booking.KBookin.exception.RateLimitExceededException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@AllArgsConstructor
@Service
public class LoginRateLimitService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MINUTES = 10;

    private final RedisTemplate<String, Integer> redisTemplate;

    private String key(String ip) {
        return "rate:login:" + ip;
    }

    public void checkRateLimit(String ip) {
        Integer attempts = redisTemplate.opsForValue().get(key(ip));
        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            throw new RateLimitExceededException(
                    "Too many login attempts. Try again later."
            );
        }
    }

    public void loginFailed(String ip) {
        Long attempts = redisTemplate.opsForValue().increment(key(ip));
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(
                    key(ip),
                    BLOCK_TIME_MINUTES,
                    TimeUnit.MINUTES
            );
        }
    }

    public void loginSuccess(String ip) {
        redisTemplate.delete(key(ip));
    }
}
