package com.example.springboot_app.global.redis.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisListKey;

@Service
public class RedisListService extends AbstractRedisService<RedisListKey> {

    public RedisListService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * List의 왼쪽에 값 추가
     */
    public Long leftPush(KeyBinding<RedisListKey> binding, String value) {
        return redisTemplate.opsForList().leftPush(binding.key(), value);
    }

    /**
     * List의 오른쪽에 값 추가
     */
    public Long rightPush(KeyBinding<RedisListKey> binding, String value) {
        return redisTemplate.opsForList().rightPush(binding.key(), value);
    }

    /**
     * List의 왼쪽에서 값 제거 및 반환
     */
    public String leftPop(KeyBinding<RedisListKey> binding) {
        return redisTemplate.opsForList().leftPop(binding.key());
    }

    /**
     * List의 오른쪽에서 값 제거 및 반환
     */
    public String rightPop(KeyBinding<RedisListKey> binding) {
        return redisTemplate.opsForList().rightPop(binding.key());
    }

    /**
     * List의 특정 범위 요소 조회
     */
    public List<String> range(KeyBinding<RedisListKey> binding, long start, long end) {
        return redisTemplate.opsForList().range(binding.key(), start, end);
    }

    /**
     * List의 특정 범위 요소 제거
     */
    public void trim(KeyBinding<RedisListKey> binding, long start, long end) {
        redisTemplate.opsForList().trim(binding.key(), start, end);
    }

    /**
     * List의 크기 조회
     */
    public Long size(KeyBinding<RedisListKey> binding) {
        return redisTemplate.opsForList().size(binding.key());
    }
}