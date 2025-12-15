package com.example.springboot_app.domain.coupon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    private static final String COUPON_STOCK_KEY = "coupon:%d:stock";
    private static final String COUPON_ISSUED_USERS_KEY = "coupon:%d:users";
    private static final String COUPON_SYNC_KEY = "coupon:sync:ids";
    private static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

    /**
     * Redis 기반 고성능 비동기 쿠폰 발급
     */
    public void issueCoupon(Long couponId, String email) {
        // 1. 유저 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        // 2. 중복 발급 체크 (Redis Set 활용)
        String userSetKey = String.format(COUPON_ISSUED_USERS_KEY, couponId);
        if (redisService.sIsMember(userSetKey, user.getId().toString())) {
            throw new BusinessException(ErrorType.ALREADY_ISSUED);
        }

        // 3. 재고 차감 (Redis Atomic DECR)
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);
        
        // 재고가 Redis에 없으면 DB에서 로드 (워밍업)
        ensureStockLoaded(couponId, stockKey);

        Long remain = redisService.decrement(stockKey);
        if (remain < 0) {
            redisService.increment(stockKey); // 차감 취소 (복구)
            throw new BusinessException(ErrorType.OUT_OF_STOCK);
        }

        // 4. 발급 처리 (비동기 동기화 큐잉)
        redisService.sAdd(userSetKey, user.getId().toString()); // 중복 방지 셋에 기록
        redisService.lPush(COUPON_ISSUE_QUEUE, user.getId() + ":" + couponId); // 발급 이력 큐
        redisService.sAdd(COUPON_SYNC_KEY, couponId.toString()); // 재고 동기화 셋
        
        log.info("Coupon issued successfully (Async). CouponId: {}, UserId: {}, Remaining: {}", couponId, user.getId(), remain);
    }

    private void ensureStockLoaded(Long couponId, String stockKey) {
        if (redisService.get(stockKey) == null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
            redisService.set(stockKey, (long) coupon.getRemainingQuantity());
        }
    }
}
