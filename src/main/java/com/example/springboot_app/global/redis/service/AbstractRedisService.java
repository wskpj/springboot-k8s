package com.example.springboot_app.global.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisKey;

public abstract class AbstractRedisService<K extends RedisKey> {

    protected final RedisTemplate<String, String> redisTemplate;

    protected AbstractRedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 키 존재 여부 확인
     */
    public Boolean hasKey(KeyBinding<K> binding) {
        return redisTemplate.hasKey(binding.key());
    }

    /**
     * 키 삭제
     */
    public Boolean delete(KeyBinding<K> binding) {
        return redisTemplate.delete(binding.key());
    }

    /**
     * 키 만료 시간 설정 (seconds)
     */
    public Boolean expire(KeyBinding<K> binding, long timeout) {
        return redisTemplate.expire(binding.key(), timeout, TimeUnit.SECONDS);
    }

}
