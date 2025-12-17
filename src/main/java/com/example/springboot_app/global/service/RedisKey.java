package com.example.springboot_app.global.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {
    COUPON_STOCK("coupon:%d:stock"),
    COUPON_SYNC_IDS("coupon:sync:ids"),
    COUPON_ISSUED_USERS("coupon:%d:users"),
    COUPON_ISSUE_QUEUE("coupon:issue:queue");

    private final String pattern;

    public String of(Object... args) {
        return (args.length == 0) ? pattern : String.format(pattern, args);
    }
}
