package com.booking.KBookin.security;

import com.booking.KBookin.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SearchRateLimitService {

    private static final int GUEST_LIMIT = 30;
    private static final int USER_LIMIT = 120;
    private static final long WINDOW_SECONDS = 60;

    private final RedisTemplate<String, Integer> redisTemplate;


    public void checkLimit(String key,int maxAllowed){
        Long count = redisTemplate.opsForValue().increment(key);

        if(count != null && count == 1){
            redisTemplate.expire(key,WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        if(count != null && count > maxAllowed){
            throw new RateLimitExceededException("Too many requests. Please try again later");
        }
    }

}