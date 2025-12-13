package com.example.springboot_app.domain.test.service;

import com.example.springboot_app.domain.test.entity.Coupon;
import com.example.springboot_app.domain.test.repository.CouponRepository;
import com.example.springboot_app.domain.test.entity.UserCoupon;
import com.example.springboot_app.domain.test.repository.UserCouponRepository;
import com.example.springboot_app.global.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 쿠폰 재고 캐싱 및 DB 동기화를 관리하는 매니저 레이어
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCacheManager {

    private final RedisService redisService;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    private static final String SYNC_KEY = "coupon:sync:ids";
    private static final String ISSUANCE_QUEUE = "coupon:issuance:queue";

    /**
     * 서버 시작 시 DB의 재고 데이터를 Redis로 자동 웜업합니다.
     */
    @PostConstruct
    @Transactional(readOnly = true)
    public void initRedisStock() {
        log.info("[CouponCacheManager] Starting initial Redis stock warm-up from DB...");
        List<Coupon> coupons = couponRepository.findAll();
        
        for (Coupon coupon : coupons) {
            String key = "coupon:" + coupon.getId() + ":stock";
            redisService.set(key, (long) coupon.getStock());
            log.info("[CouponCacheManager] Warmed up coupon {}: stock={}", coupon.getId(), coupon.getStock());
        }
        
        log.info("[CouponCacheManager] Initial Redis stock warm-up completed.");
    }

    /**
     * 1초마다 Redis에서 변경이 발생한 쿠폰 ID들만 골라내어 DB로 동기화합니다.
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void syncStockToDb() {
        Set<Object> syncIds = redisService.sMembers(SYNC_KEY);
        
        if (syncIds == null || syncIds.isEmpty()) {
            return;
        }

        log.debug("[CouponCacheManager] Starting targeted sync for {} coupons...", syncIds.size());
        
        redisService.delete(SYNC_KEY);
        
        for (Object idObj : syncIds) {
            Long couponId = Long.valueOf(idObj.toString());
            String key = "coupon:" + couponId + ":stock";
            Object redisStockObj = redisService.get(key);
            
            if (redisStockObj != null) {
                Long redisStock = Long.valueOf(redisStockObj.toString());
                log.info("[CouponCacheManager] Targeted Sync: coupon {} -> stock {}", couponId, redisStock);
                couponRepository.updateStock(couponId, redisStock.intValue());
            }
        }
    }

    /**
     * 1초마다 Redis 큐에서 발급 이력을 가져와 DB에 벌크 저장합니다.
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void syncUserCouponsToDb() {
        List<Object> rawEvents = redisService.lRange(ISSUANCE_QUEUE, 0, 300);
        
        if (rawEvents == null || rawEvents.isEmpty()) {
            return;
        }

        log.info("[CouponCacheManager] Syncing {} user coupons to DB...", rawEvents.size());
        
        List<UserCoupon> userCoupons = new ArrayList<>();
        for (Object eventObj : rawEvents) {
            String event = eventObj.toString();
            String[] parts = event.split(":");
            if (parts.length == 2) {
                String email = parts[0];
                Long couponId = Long.valueOf(parts[1]);
                userCoupons.add(new UserCoupon(email, couponId));
            }
        }

        if (!userCoupons.isEmpty()) {
            userCouponRepository.saveAll(userCoupons);
            // 저장 성공 시 큐에서 처리한 만큼 제거
            redisService.lTrim(ISSUANCE_QUEUE, rawEvents.size(), -1);
            log.info("[CouponCacheManager] Successfully synced {} user coupons.", userCoupons.size());
        }
    }
}
