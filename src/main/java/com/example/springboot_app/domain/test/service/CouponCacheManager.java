package com.example.springboot_app.domain.test.service;

import com.example.springboot_app.domain.test.entity.Coupon;
import com.example.springboot_app.domain.test.repository.CouponRepository;
import com.example.springboot_app.global.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 쿠폰 재고 캐싱 및 DB 동기화를 관리하는 매니저 레이어
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCacheManager {

    private final RedisService redisService;
    private final CouponRepository couponRepository;

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
     * 1초마다 Redis의 재고 데이터를 DB로 동기화합니다.
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void syncStockToDb() {
        log.debug("[CouponCacheManager] Starting automatic Redis to DB stock synchronization...");
        
        List<Coupon> coupons = couponRepository.findAll();
        
        for (Coupon coupon : coupons) {
            String key = "coupon:" + coupon.getId() + ":stock";
            Object redisStockObj = redisService.get(key);
            
            if (redisStockObj != null) {
                Long redisStock = Long.valueOf(redisStockObj.toString());
                if (redisStock.intValue() != coupon.getStock()) {
                    log.info("[CouponCacheManager] Syncing coupon {}: Redis ({}) -> DB ({})", coupon.getId(), redisStock, coupon.getStock());
                    couponRepository.updateStock(coupon.getId(), redisStock.intValue());
                }
            }
        }
    }
}
