package com.example.springboot_app.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.redis.CouponRedisRepository;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.global.exception.enums.BusinessError;
import com.example.springboot_app.global.exception.types.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRedisRepository couponRedisRepository;
    private final CouponRepository couponRepository;

    /**
     * Redis 기반 고성능 비동기 쿠폰 발급
     */
    public void issueCoupon(Long couponId, Long userId) {
        // 1. 중복 발급 체크 (Redis Set 활용)
        if (couponRedisRepository.isAlreadyIssued(couponId, userId)) {
            throw new BusinessException(BusinessError.ALREADY_ISSUED);
        }

        // 2. 재고 로드 (워밍업 확인)
        ensureStockLoaded(couponId);

        // 3. 재고 차감 (Redis Atomic DECR)
        Long remain = couponRedisRepository.decreaseStock(couponId);
        if (remain < 0) {
            couponRedisRepository.increaseStock(couponId); // 차감 취소 (복구)
            throw new BusinessException(BusinessError.OUT_OF_STOCK);
        }

        // 4. 발급 처리 (비동기 동기화 큐잉)
        couponRedisRepository.addIssuedUser(couponId, userId);
        couponRedisRepository.addIssueEvent(userId, couponId);
        couponRedisRepository.addSyncId(couponId);
        
        log.info("Coupon issued successfully (Async). CouponId: {}, UserId: {}, Remaining: {}", couponId, userId, remain);
    }

    private void ensureStockLoaded(Long couponId) {
        if (couponRedisRepository.getStock(couponId) == null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new BusinessException(BusinessError.COUPON_NOT_FOUND));
            couponRedisRepository.initializeStock(couponId, coupon.getRemainingQuantity());
        }
    }
}
