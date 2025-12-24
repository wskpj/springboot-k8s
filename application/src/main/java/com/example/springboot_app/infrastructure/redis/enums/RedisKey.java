package com.example.springboot_app.infrastructure.redis.enums;

import com.example.springboot_app.infrastructure.redis.dto.KeyBinding;

public interface RedisKey {
    String getPattern();

    @SuppressWarnings("unchecked")
    default <K extends RedisKey> KeyBinding<K> bind(Object... args) {
        return (KeyBinding<K>) new KeyBinding<>(this, String.format(getPattern(), args));
    }
}
