package com.example.springboot_app.global.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisLuaScript {
    RATE_LIMIT("rate_limit.lua", Long.class);

    private final String fileName;
    private final Class<?> resultType;
    private final String path = "scripts/";

    public String getFullPath() {
        return path + fileName;
    }
}
