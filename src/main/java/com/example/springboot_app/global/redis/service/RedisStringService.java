
package com.example.springboot_app.global.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisStringKey;

@Service
public class RedisStringService extends AbstractRedisService<RedisStringKey> {

    public RedisStringService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * String 값 설정
     */
    public void set(KeyBinding<RedisStringKey> binding, String value) {
        redisTemplate.opsForValue().set(binding.key(), value);
    }

    /**
     * String 값 설정 (with timeout)
     */
    public void set(KeyBinding<RedisStringKey> binding, String value, long timeout) {
        redisTemplate.opsForValue().set(binding.key(), value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 값 조회
     */
    public String get(KeyBinding<RedisStringKey> binding) {
        return redisTemplate.opsForValue().get(binding.key());
    }

    /**
     * 값 조회 (with type casting)
     */
    public <T> T get(KeyBinding<RedisStringKey> binding, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(binding.key());
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * String 값 설정 (if not exists)
     */
    public boolean setIfAbsent(KeyBinding<RedisStringKey> binding, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(binding.key(), value));
    }

    /**
     * String 값 설정 (if not exists with timeout)
     */
    public boolean setIfAbsent(KeyBinding<RedisStringKey> binding, String value, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(binding.key(), value, timeout, TimeUnit.SECONDS));
    }

    /**
     * 값 증가 (default: 1)
     */
    public Long increment(KeyBinding<RedisStringKey> binding) {
        return increment(binding, 1L);
    }

    public Long increment(KeyBinding<RedisStringKey> binding, long delta) {
        return redisTemplate.opsForValue().increment(binding.key(), delta);
    }

    /**
     * 값 감소 (default: 1)
     */
    public Long decrement(KeyBinding<RedisStringKey> binding) {
        return decrement(binding, 1L);
    }

    public Long decrement(KeyBinding<RedisStringKey> binding, long delta) {
        return redisTemplate.opsForValue().increment(binding.key(), -delta);
    }
}