package com.example.springboot_app.domain.admin.service;

import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        
        // Redis 재고 즉시 초기화 (웜업 효과)
        String stockKey = String.format(COUPON_STOCK_KEY, savedCoupon.getId());
        redisService.set(stockKey, (long) totalQuantity);
        
        log.info("[AdminService] New coupon created and warmed up. CouponId: {}, Title: {}, Stock: {}", 
                savedCoupon.getId(), title, totalQuantity);
                
        return savedCoupon;
    }
}
