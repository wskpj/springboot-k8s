package com.example.springboot_app.global.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisStringKey implements RedisKey {

    COUPON_STOCK("coupon:%d:stock");

    private final String pattern;

}
