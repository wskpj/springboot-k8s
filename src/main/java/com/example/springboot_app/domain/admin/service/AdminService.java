package com.example.springboot_app.domain.admin.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final CouponRepository couponRepository;
    private final RedisService redisService;

    private static final String COUPON_STOCK_KEY = "coupon:%s:stock";

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
        String stockKey = String.format(COUPON_STOCK_KEY, savedCoupon.getId());
        redisService.set(stockKey, (long) totalQuantity);
        
        log.info("[AdminService] New coupon created and warmed up. CouponId: {}, Title: {}, Stock: {}", 
                savedCoupon.getId(), title, totalQuantity);
                
        return savedCoupon;
    }

    /**
     * Redis에서 실시간 재고 조회
     */
    public int getCouponStock(Long couponId) {
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);
        Object stockObj = redisService.get(stockKey);
        
        if (stockObj != null) {
            return Integer.parseInt(stockObj.toString());
        }

        // Redis에 없으면 DB에서 조회 (방어적 코드)
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        
        return coupon.getRemainingQuantity();
    }

    /**
     * 특정 쿠폰의 재고를 DB 값으로 강제 초기화
     */
    @Transactional
    public void refreshCouponStock(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);
        redisService.set(stockKey, (long) coupon.getRemainingQuantity());
        
        log.info("[AdminService] Forcefully refreshed coupon {} stock to DB value: {}", 
                couponId, coupon.getRemainingQuantity());
    }

    /**
     * 현재 Redis의 재고 변경사항을 즉시 DB에 반영
     */
    @Transactional
    public void syncStockNow() {
        log.info("[AdminService] Manual stock synchronization triggered.");
        // 관리자용 명시적 동기화 로직은 CouponCacheManager의 동기화 로직을 그대로 재사용하거나
        // 직접 모든 쿠폰 키를 순회하며 반영할 수 있습니다.
        // 현재는 Redis의 'coupon:sync:ids' 세트를 그대로 활용합니다.
        
        Set<Object> syncIds = redisService.sMembers("coupon:sync:ids");
        if (syncIds == null || syncIds.isEmpty()) {
            log.info("[AdminService] No stock changes to sync.");
            return;
        }

        for (Object idObj : syncIds) {
            String idStr = idObj.toString();
            String stockKey = String.format(COUPON_STOCK_KEY, idStr);
            Object stockObj = redisService.get(stockKey);
            
            if (stockObj != null) {
                Long remaining = Long.valueOf(stockObj.toString());
                couponRepository.updateRemainingQuantity(Long.valueOf(idStr), remaining.intValue());
            }
        }
        redisService.delete("coupon:sync:ids");
        log.info("[AdminService] Successfully synced stock for {} coupons.", syncIds.size());
    }
}
