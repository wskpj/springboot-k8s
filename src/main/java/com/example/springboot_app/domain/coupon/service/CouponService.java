package com.example.springboot_app.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.springboot_app.domain.coupon.exception.CouponException;
import com.example.springboot_app.domain.coupon.redis.CouponRedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRedisRepository couponRedisRepository;

    /**
     * Redis 기반 고성능 비동기 쿠폰 발급
     */
    public void issueCoupon(Long couponId, Long userId) {
        // 1. 원자적 발급 처리 (Lua Script)
        // 0: 성공, -101: 중복 발급, -102: 재고 부족
        Long result = couponRedisRepository.issueCoupon(couponId, userId);
        if (result == -101) throw new CouponException.AlreadyIssued();
        if (result == -102) throw new CouponException.OutOfStock(couponId);

        log.info("Coupon issued successfully. CouponId: {}, UserId: {}", couponId, userId);
    }
}
