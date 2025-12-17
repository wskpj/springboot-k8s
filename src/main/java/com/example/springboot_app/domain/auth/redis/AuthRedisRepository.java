package com.example.springboot_app.domain.auth.redis;

import org.springframework.stereotype.Repository;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.dto.ValueBinding;
import com.example.springboot_app.global.redis.enums.RedisSetKey;
import com.example.springboot_app.global.redis.service.RedisSetService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepository {

    private final RedisSetService redisSetService;

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
}
