package com.example.springboot_app.global.redis.enums;

import com.example.springboot_app.global.redis.dto.KeyBinding;

public interface RedisKey {
    String getPattern();

    default <K extends RedisKey> KeyBinding<K> bind(Object... args) {
        return new KeyBinding<>((K) this, String.format(getPattern(), args));
    }
}
