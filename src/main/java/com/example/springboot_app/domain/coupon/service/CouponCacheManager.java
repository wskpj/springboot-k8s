package com.example.springboot_app.domain.coupon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.entity.UserCoupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.repository.UserCouponRepository;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.service.RedisService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCacheManager {

    private final RedisService redisService;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    private static final String COUPON_STOCK_KEY = "coupon:%s:stock";
    private static final String COUPON_SYNC_KEY = "coupon:sync:ids";
    private static final String COUPON_ISSUE_QUEUE = "coupon:issue:queue";

    /**
     * 서버 기동 시 현재 재고 데이터를 Redis로 웜업
     */
    @PostConstruct
    @Transactional(readOnly = true)
    public void warmup() {
        log.info("[CouponCacheManager] Warming up coupon stock to Redis...");
        List<Coupon> coupons = couponRepository.findAll();
        for (Coupon coupon : coupons) {
            String stockKey = String.format(COUPON_STOCK_KEY, coupon.getId());
            redisService.set(stockKey, (long) coupon.getRemainingQuantity());
            log.info("[CouponCacheManager] Warmed up coupon {}: stock={}", coupon.getId(), coupon.getRemainingQuantity());
        }
    }

    /**
     * 1초마다 변경된 재고를 DB에 반영 (Async Sync)
     */
    @Scheduled(fixedDelay = 1000)
    public void syncStockToDb() {
        Set<Object> syncIds = redisService.sMembers(COUPON_SYNC_KEY);
        if (syncIds == null || syncIds.isEmpty()) return;

        log.debug("[CouponCacheManager] Syncing stock for {} coupons...", syncIds.size());
        redisService.delete(COUPON_SYNC_KEY);

        for (Object idObj : syncIds) {
            String idStr = idObj.toString();
            String stockKey = String.format(COUPON_STOCK_KEY, idStr);
            Object stockObj = redisService.get(stockKey);
            
            if (stockObj != null) {
                Long remaining = Long.valueOf(stockObj.toString());
                couponRepository.updateRemainingQuantity(Long.valueOf(idStr), remaining.intValue());
            }
        }
    }

    /**
     * 1초마다 발급 큐를 확인하여 DB에 벌크 저장
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void syncIssuanceToDb() {
        // 한 번에 최대 500개씩 처리
        List<Object> events = redisService.lRange(COUPON_ISSUE_QUEUE, 0, 499);
        if (events == null || events.isEmpty()) return;

        log.info("[CouponCacheManager] Syncing {} user coupons to DB...", events.size());
        
        List<UserCoupon> issues = new ArrayList<>();
        for (Object eventObj : events) {
            String event = eventObj.toString(); // format: "userId:couponId"
            String[] parts = event.split(":");
            if (parts.length == 2) {
                Long userId = Long.valueOf(parts[0]);
                Long couponId = Long.valueOf(parts[1]);
                
                // getReferenceById를 통해 DB 조회 없이 프록시만 생성 (초고성능 발급 이력 저장)
                issues.add(UserCoupon.builder()
                        .user(userRepository.getReferenceById(userId))
                        .coupon(couponRepository.getReferenceById(couponId))
                        .build());
            }
        }

        if (!issues.isEmpty()) {
            userCouponRepository.saveAll(issues);
            // 처리한 만큼 큐에서 제거
            redisService.lTrim(COUPON_ISSUE_QUEUE, events.size(), -1);
            log.info("[CouponCacheManager] Successfully synced {} user coupons.", issues.size());
        }
    }
}
