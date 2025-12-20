
package com.example.springboot_app.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_app.infrastructure.redis.dto.KeyBinding;
import com.example.springboot_app.infrastructure.redis.dto.ValueBinding;
import com.example.springboot_app.infrastructure.redis.enums.RedisStringKey;

@Service
public class RedisStringService extends AbstractRedisService<RedisStringKey> {

    public RedisStringService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * String 값 설정
     */
    public void set(KeyBinding<RedisStringKey> b, ValueBinding v) {
        redisTemplate.opsForValue().set(b.key(), v.value());
    }

    /**
     * String 값 설정 (with timeout)
     */
    public void set(KeyBinding<RedisStringKey> b, ValueBinding v, long timeout) {
        redisTemplate.opsForValue().set(b.key(), v.value(), timeout, TimeUnit.SECONDS);
    }

    /**
     * 값 조회
     */
    public String get(KeyBinding<RedisStringKey> b) {
        return redisTemplate.opsForValue().get(b.key());
    }

    /**
     * 값 조회 (with type casting)
     */
    public <T> T get(KeyBinding<RedisStringKey> b, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(b.key());
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * String 값 설정 (if not exists)
     */
    public boolean setIfAbsent(KeyBinding<RedisStringKey> b, ValueBinding v) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(b.key(), v.value()));
    }

    /**
     * String 값 설정 (if not exists with timeout)
     */
    public boolean setIfAbsent(KeyBinding<RedisStringKey> b, ValueBinding v, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(b.key(), v.value(), timeout, TimeUnit.SECONDS));
    }

    /**
     * 값 증가 (default: 1)
     */
    public Long increment(KeyBinding<RedisStringKey> b) {
        return increment(b, 1L);
    }

    public Long increment(KeyBinding<RedisStringKey> b, long delta) {
        return redisTemplate.opsForValue().increment(b.key(), delta);
    }

    /**
     * 값 감소 (default: 1)
     */
    public Long decrement(KeyBinding<RedisStringKey> b) {
        return decrement(b, 1L);
    }

    public Long decrement(KeyBinding<RedisStringKey> b, long delta) {
        return redisTemplate.opsForValue().increment(b.key(), -delta);
    }
}