package com.example.springboot_app.domain.coupon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.entity.UserCoupon;
import com.example.springboot_app.domain.coupon.redis.CouponRedisRepository;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.repository.UserCouponRepository;
import com.example.springboot_app.domain.user.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCacheManager {

    private final CouponRedisRepository couponRedisRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    /**
     * 서버 기동 시 현재 재고 데이터를 Redis로 웜업
     */
    @PostConstruct
    public void warmupStock() {
        log.info("[CouponCacheManager] Warming up coupon stock to Redis...");
        List<Coupon> coupons = couponRepository.findAll();
        for (Coupon coupon : coupons) {
            boolean initialized = couponRedisRepository.initializeStock(coupon.getId(), coupon.getRemainingQuantity());
            
            if (initialized) {
                log.info("[CouponCacheManager] Warmed up coupon {}: stock={}", coupon.getId(), coupon.getRemainingQuantity());
            } else {
                log.info("[CouponCacheManager] Coupon {} already has stock in Redis, skipping warmup to protect live data.", coupon.getId());
            }
        }
    }

    /**
     * 주기적으로 Redis의 재고 변경사항을 DB에 싱크 (Write-Back)
     */
    @Scheduled(fixedDelay = 1000)
    public void syncStockToDb() {
        Set<String> syncIds = couponRedisRepository.getAndClearSyncIds();
        if (syncIds == null || syncIds.isEmpty()) return;

        log.debug("[CouponCacheManager] Syncing stock for {} coupons...", syncIds.size());

        for (String idStr : syncIds) {
            Long couponId = Long.valueOf(idStr);
            Integer remaining = couponRedisRepository.getStock(couponId);
            
            if (remaining != null) {
                couponRepository.updateRemainingQuantity(couponId, remaining);
            }
        }
    }

    /**
     * 비동기 발급 큐를 소모하여 DB에 저장
     */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void syncIssuanceToDb() {
        // 한 번에 최대 500개씩 처리
        List<String> events = couponRedisRepository.getIssueEvents(500);
        if (events == null || events.isEmpty()) return;

        log.info("[CouponCacheManager] Syncing {} user coupons to DB...", events.size());
        
        List<UserCoupon> issues = new ArrayList<>();
        for (String event : events) {
            String[] parts = event.split(":"); // format: "userId:couponId"
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
            couponRedisRepository.trimIssueEvents(events.size());
            log.info("[CouponCacheManager] Successfully synced {} user coupons.", issues.size());
        }
    }
}
