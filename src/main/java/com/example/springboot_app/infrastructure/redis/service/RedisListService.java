package com.example.springboot_app.infrastructure.redis.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_app.infrastructure.redis.dto.KeyBinding;
import com.example.springboot_app.infrastructure.redis.dto.ValueBinding;
import com.example.springboot_app.infrastructure.redis.enums.RedisListKey;

@Service
public class RedisListService extends AbstractRedisService<RedisListKey> {

    public RedisListService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * List의 왼쪽에 값 추가
     */
    public Long leftPush(KeyBinding<RedisListKey> k, ValueBinding v) {
        return redisTemplate.opsForList().leftPush(k.key(), v.value());
    }

    /**
     * List의 오른쪽에 값 추가
     */
    public Long rightPush(KeyBinding<RedisListKey> k, ValueBinding v) {
        return redisTemplate.opsForList().rightPush(k.key(), v.value());
    }

    /**
     * List의 왼쪽에서 값 제거 및 반환
     */
    public String leftPop(KeyBinding<RedisListKey> k) {
        return redisTemplate.opsForList().leftPop(k.key());
    }

    /**
     * List의 오른쪽에서 값 제거 및 반환
     */
    public String rightPop(KeyBinding<RedisListKey> k) {
        return redisTemplate.opsForList().rightPop(k.key());
    }

    /**
     * List의 특정 범위 요소 조회
     */
    public List<String> range(KeyBinding<RedisListKey> k, long start, long end) {
        return redisTemplate.opsForList().range(k.key(), start, end);
    }

    /**
     * List의 특정 범위 요소 제거
     */
    public void trim(KeyBinding<RedisListKey> k, long start, long end) {
        redisTemplate.opsForList().trim(k.key(), start, end);
    }

    /**
     * List의 크기 조회
     */
    public Long size(KeyBinding<RedisListKey> k) {
        return redisTemplate.opsForList().size(k.key());
    }
}