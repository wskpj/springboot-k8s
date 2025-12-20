package com.example.springboot_app.infrastructure.redis.repository;

import org.springframework.stereotype.Repository;

import com.example.springboot_app.domain.auth.redis.AuthRedisRepository;
import com.example.springboot_app.infrastructure.redis.annotation.LuaExecute;
import com.example.springboot_app.infrastructure.redis.dto.KeyBinding;
import com.example.springboot_app.infrastructure.redis.dto.ValueBinding;
import com.example.springboot_app.infrastructure.redis.enums.RedisLuaScript;
import com.example.springboot_app.infrastructure.redis.enums.RedisSetKey;
import com.example.springboot_app.infrastructure.redis.enums.RedisStringKey;
import com.example.springboot_app.infrastructure.redis.service.RedisLuaScriptExecutor;
import com.example.springboot_app.infrastructure.redis.service.RedisSetService;
import com.example.springboot_app.infrastructure.redis.service.RedisStringService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepositoryImpl implements AuthRedisRepository {

    private final RedisSetService redisSetService;
    private final RedisStringService redisStringService;
    private final RedisLuaScriptExecutor scriptExecutor;

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

    @LuaExecute
    public Long incrementLoginFailCount(String email, long lockDurationSeconds) {
        // 1. Bind key
        KeyBinding<RedisStringKey> key = RedisStringKey.LOGIN_FAIL_COUNT.bind(email);

        // 2. Execute script
        return scriptExecutor.execute(
                RedisLuaScript.RATE_LIMIT,
                key,
                lockDurationSeconds);
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
