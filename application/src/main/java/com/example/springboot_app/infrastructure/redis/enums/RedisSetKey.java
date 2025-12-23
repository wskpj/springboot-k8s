package com.example.springboot_app.infrastructure.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisSetKey implements RedisKey {

    COUPON_SYNC_IDS("coupon:sync:ids"),
    COUPON_ISSUED_USERS("coupon:%d:users"),
    REFRESH_TOKENS("user:%d:refresh_tokens");

    private final String pattern;
}
