package com.example.springboot_app.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.global.exception.enums.BusinessError;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final RedisService redisService;

    private static final String COUPON_STOCK_KEY = "coupon:%d:stock";
    private static final String COUPON_ISSUED_USERS_KEY = "coupon:%d:users";
    private static final String COUPON_SYNC_KEY = "coupon:sync:ids";
    private static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

    /**
     * Redis 기반 고성능 비동기 쿠폰 발급
     */
    public void issueCoupon(Long couponId, Long userId) {
        // 1. 중복 발급 체크 (Redis Set 활용)
        String userSetKey = String.format(COUPON_ISSUED_USERS_KEY, couponId);
        if (redisService.sAdd(userSetKey, userId.toString()) == 0) {
            throw new BusinessException(BusinessError.ALREADY_ISSUED);
        }

        // 2. 재고 차감 (Redis Atomic DECR)
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);

        Long remain = redisService.decrement(stockKey);
        if (remain < 0) {
            redisService.increment(stockKey); // 차감 취소 (복구)
            throw new BusinessException(BusinessError.OUT_OF_STOCK);
        }

        // 3. 발급 처리 (비동기 동기화 큐잉)
        redisService.sAdd(userSetKey, userId.toString()); // 중복 방지 셋에 기록
        redisService.lPush(COUPON_ISSUE_QUEUE, userId + ":" + couponId); // 발급 이력 큐
        redisService.sAdd(COUPON_SYNC_KEY, couponId.toString()); // 재고 동기화 셋
        
        log.info("Coupon issued successfully (Async). CouponId: {}, UserId: {}, Remaining: {}", couponId, userId, remain);
    }
}
