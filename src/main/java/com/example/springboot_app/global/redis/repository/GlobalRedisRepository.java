package com.example.springboot_app.global.redis.repository;

import org.springframework.stereotype.Repository;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisLuaScript;
import com.example.springboot_app.global.redis.enums.RedisStringKey;
import com.example.springboot_app.global.redis.service.RedisLuaScriptExecutor;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GlobalRedisRepository {

    private final RedisLuaScriptExecutor scriptExecutor;

    /**
     * 루아 스크립트를 이용한 원자적 Rate Limit 체크 및 카운트 증가
     */
    public Long checkAndIncrementRateLimit(String identifier, String uri, long duration) {
        KeyBinding<RedisStringKey> key = RedisStringKey.RATE_LIMIT.bind(identifier, uri);
        return scriptExecutor.execute(RedisLuaScript.RATE_LIMIT, key, duration);
    }
}
