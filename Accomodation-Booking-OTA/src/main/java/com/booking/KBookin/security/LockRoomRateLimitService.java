package com.booking.KBookin.security;

import com.booking.KBookin.exception.RateLimitExceededException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
public class LockRoomRateLimitService {

    private static final int USER_LIMIT = 5;
    private static final long WINDOW_TIME_MINUTE = 3;
    private final RedisTemplate<String, Integer> redisTemplate;


    public void checkLimit(String key){
        Long count = redisTemplate.opsForValue().increment(key);

        if(count != null && count == 1){
            redisTemplate.expire(key,WINDOW_TIME_MINUTE, TimeUnit.MINUTES);
        }
        if(count != null && count > USER_LIMIT){
            throw new RateLimitExceededException("Too many requests for room reservation. Please try again later");
        }
    }
}
