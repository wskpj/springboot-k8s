package com.example.springboot_app.global.redis.service;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.dto.ValueBinding;
import com.example.springboot_app.global.redis.enums.RedisSetKey;

@Service
public class RedisSetService extends AbstractRedisService<RedisSetKey> {

    public RedisSetService(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * Set에 값 추가
     */
    public Long add(KeyBinding<RedisSetKey> b,ValueBinding v) {
        return redisTemplate.opsForSet().add(b.key(), v.value());
    }

    /**
     * Set의 모든 멤버 조회
     */
    public Set<String> members(KeyBinding<RedisSetKey> b) {
        return redisTemplate.opsForSet().members(b.key());
    }

    /**
     * Set의 멤버 수 조회
     */
    public Long size(KeyBinding<RedisSetKey> b) {
        return redisTemplate.opsForSet().size(b.key());
    }

    /**
     * Set에서 값 제거
     */
    public Long remove(KeyBinding<RedisSetKey> b, ValueBinding v) {
        return redisTemplate.opsForSet().remove(b.key(), v.value());
    }

    /**
     * Set에 특정 값이 있는지 확인
     */
    public Boolean isMember(KeyBinding<RedisSetKey> b,ValueBinding v) {
        return redisTemplate.opsForSet().isMember(b.key(), v.value());
    }
}