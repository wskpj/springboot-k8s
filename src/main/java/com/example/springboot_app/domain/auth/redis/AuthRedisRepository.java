package com.example.springboot_app.domain.auth.redis;

import org.springframework.stereotype.Repository;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.dto.ValueBinding;
import com.example.springboot_app.global.redis.enums.RedisSetKey;
import com.example.springboot_app.global.redis.enums.RedisStringKey;
import com.example.springboot_app.global.redis.service.RedisSetService;
import com.example.springboot_app.global.redis.service.RedisStringService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepository {

    private final RedisSetService redisSetService;
    private final RedisStringService redisStringService;

    public void addRefreshToken(Long userId, String refreshToken, long ttlSeconds) {
        KeyBinding<RedisSetKey> key = RedisSetKey.REFRESH_TOKENS.bind(userId);
        ValueBinding value = ValueBinding.of(refreshToken);        
        redisSetService.add(key, value);
        redisSetService.expire(key, ttlSeconds);
    }

    public boolean isValidRefreshToken(Long userId, String refreshToken) {
        KeyBinding<RedisSetKey> key = RedisSetKey.REFRESH_TOKENS.bind(userId);
        ValueBinding value = ValueBinding.of(refreshToken);
        return redisSetService.isMember(key, value);
    }

    public void removeRefreshToken(Long userId, String refreshToken) {
        KeyBinding<RedisSetKey> key = RedisSetKey.REFRESH_TOKENS.bind(userId);
        ValueBinding value = ValueBinding.of(refreshToken);
        redisSetService.remove(key, value);
    }

    public void removeAllRefreshTokens(Long userId) {
        KeyBinding<RedisSetKey> key = RedisSetKey.REFRESH_TOKENS.bind(userId);
        redisSetService.delete(key);
    }

    public long incrementLoginFailCount(String email, long lockDurationSeconds) {
        KeyBinding<RedisStringKey> key = RedisStringKey.LOGIN_FAIL_COUNT.bind(email);
        Long count = redisStringService.increment(key);
        if (count != null && count == 1) {
            redisStringService.expire(key, lockDurationSeconds);
        }
        return count != null ? count : 0;
    }

    public int getLoginFailCount(String email) {
        KeyBinding<RedisStringKey> key = RedisStringKey.LOGIN_FAIL_COUNT.bind(email);
        String count = redisStringService.get(key);
        return count != null ? Integer.parseInt(count) : 0;
    }

    public void clearLoginFailCount(String email) {
        KeyBinding<RedisStringKey> key = RedisStringKey.LOGIN_FAIL_COUNT.bind(email);
        redisStringService.delete(key);
    }
}
