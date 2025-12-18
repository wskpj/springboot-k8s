package com.example.springboot_app.global.redis.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisLuaScript;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLuaScriptExecutor {
    private final RedisTemplate<String, String> redisTemplate;
    private final Map<RedisLuaScript, RedisScript<?>> scriptCache = new ConcurrentHashMap<>();

    @SuppressWarnings({ "unchecked", "null" })
    public <T> T execute(RedisLuaScript script, List<KeyBinding<?>> bindings, List<Object> values) {
        RedisScript<T> redisScript = (RedisScript<T>) scriptCache
                .computeIfAbsent(script, s -> {
                    DefaultRedisScript<Object> rs = new DefaultRedisScript<>();
                    rs.setLocation(new ClassPathResource(s.getFullPath()));
                    rs.setResultType((Class<Object>) s.getResultType());
                    return rs;
                });
        return redisTemplate.execute(
                redisScript,
                bindings.stream().map(KeyBinding::key).toList(),
                values.toArray());
    }

    public <T> T execute(RedisLuaScript script, KeyBinding<?> binding, Object... values) {
        return execute(script, List.of(binding), List.of(values));
    }
}
