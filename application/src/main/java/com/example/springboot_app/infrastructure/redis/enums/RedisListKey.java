
package com.example.springboot_app.infrastructure.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisListKey implements RedisKey {

    COUPON_ISSUE_QUEUE("coupon:issue:queue");

    private final String pattern;

}
