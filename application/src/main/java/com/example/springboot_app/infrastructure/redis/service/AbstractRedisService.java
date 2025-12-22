package com.example.springboot_app.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.example.springboot_app.infrastructure.redis.dto.KeyBinding;
import com.example.springboot_app.infrastructure.redis.enums.RedisKey;

public abstract class AbstractRedisService<K extends RedisKey> {

    protected final RedisTemplate<String, String> redisTemplate;

    protected AbstractRedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 키 존재 여부 확인
     */
    public Boolean hasKey(KeyBinding<K> b) {
        return redisTemplate.hasKey(b.key());
    }

    /**
     * 키 삭제
     */
    public Boolean delete(KeyBinding<K> b) {
        return redisTemplate.delete(b.key());
    }

    /**
     * 키 만료 시간 설정 (seconds)
     */
    public Boolean expire(KeyBinding<K> b, long timeout) {
        return redisTemplate.expire(b.key(), timeout, TimeUnit.SECONDS);
    }

    /**
     * 키 만료 시간 조회 (seconds)
     */
    public Long getExpire(KeyBinding<K> b) {
        return redisTemplate.getExpire(b.key(), TimeUnit.SECONDS);
    }

}
