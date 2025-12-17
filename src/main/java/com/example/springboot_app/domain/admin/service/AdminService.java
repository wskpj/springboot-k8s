package com.example.springboot_app.domain.admin.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AdminService {

    private final CouponRepository couponRepository;
    private final CouponRedisRepository couponRedisRepository;

    /**
     * 새로운 쿠폰 생성 및 Redis 재고 웜업
     */
    @Transactional
    public Coupon createCoupon(String title, Integer totalQuantity) {
        Coupon coupon = Coupon.builder()
                .title(title)
                .totalQuantity(totalQuantity)
                .build();
        
        Coupon savedCoupon = couponRepository.save(coupon);
        
        // Redis 재고 즉시 초기화
        couponRedisRepository.setStock(savedCoupon.getId(), totalQuantity);
        
        log.info("[AdminService] New coupon created and warmed up. CouponId: {}, Title: {}, Stock: {}", 
                savedCoupon.getId(), title, totalQuantity);
                
        return savedCoupon;
    }

    /**
     * Redis에서 실시간 재고 조회
     */
    public int getCouponStock(Long couponId) {
        Integer stock = couponRedisRepository.getStock(couponId);
        
        if (stock != null) {
            return stock;
        }

        // Redis에 없으면 DB에서 조회 (방어적 코드)
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(BusinessError.COUPON_NOT_FOUND));
        
        return coupon.getRemainingQuantity();
    }

    /**
     * 특정 쿠폰의 재고를 DB 값으로 강제 초기화
     */
    @Transactional
    public void refreshCouponStock(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(BusinessError.COUPON_NOT_FOUND));
        
        couponRedisRepository.setStock(couponId, coupon.getRemainingQuantity());
        
        log.info("[AdminService] Forcefully refreshed coupon {} stock to DB value: {}", 
                couponId, coupon.getRemainingQuantity());
    }

    /**
     * 현재 Redis의 재고 변경사항을 즉시 DB에 반영
     */
    @Transactional
    public void syncStockNow() {
        log.info("[AdminService] Manual stock synchronization triggered.");
        
        Set<String> syncIds = couponRedisRepository.getAndClearSyncIds();
        if (syncIds == null || syncIds.isEmpty()) {
            log.info("[AdminService] No stock changes to sync.");
            return;
        }

        for (String idStr : syncIds) {
            Long couponId = Long.valueOf(idStr);
            Integer remaining = couponRedisRepository.getStock(couponId);
            
            if (remaining != null) {
                couponRepository.updateRemainingQuantity(couponId, remaining);
            }
        }
        log.info("[AdminService] Successfully synced stock for {} coupons.", syncIds.size());
    }
}
