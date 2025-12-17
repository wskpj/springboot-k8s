package com.example.springboot_app.global.redis.dto;

import com.example.springboot_app.global.redis.enums.RedisKey;

public record KeyBinding<K extends RedisKey>(K metadata, String actualKey) {
    public String key() {
        return actualKey;
    }
}