package com.example.springboot_app.api.test;

import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.test.entity.Coupon;
import com.example.springboot_app.domain.test.entity.UserCoupon;
import com.example.springboot_app.domain.test.repository.CouponRepository;
import com.example.springboot_app.domain.test.repository.UserCouponRepository;
import com.example.springboot_app.global.service.RedisService;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.enums.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoadTestController implements LoadTestApi {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisService redisService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String cpuIntensiveBcrypt(int rounds) {
        long start = System.currentTimeMillis();
        String result = "load-test-seed";
        for (int i = 0; i < rounds; i++) {
            result = encoder.encode(result);
        }
        long elapsed = System.currentTimeMillis() - start;
        return "Burned CPU using BCrypt. Elapsed: " + elapsed + "ms";
    }

    @Override
    public String cpuIntensiveFactorial(int n) {
        long start = System.currentTimeMillis();
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        long elapsed = System.currentTimeMillis() - start;
        return "Burned CPU using Factorial. Elapsed: " + elapsed + "ms";
    }

    @Override
    @Transactional
    public String dbWriteIntensive(int count) {
        for (int i = 0; i < count; i++) {
            String uuid = UUID.randomUUID().toString();
            User dummyUser = User.builder()
                    .email("stress_" + uuid + "@test.com")
                    .password("dummy")
                    .name("StressTestUser")
                    .build();
            userRepository.save(dummyUser);
        }
        return "Burned DB via Writes.";
    }

    @Override
    @Transactional(readOnly = true)
    public String dbReadIntensive(int iterations) {
        long totalUsersScanned = 0;
        for (int i = 0; i < iterations; i++) {
            totalUsersScanned += userRepository.count();
        }
        return "Burned DB via Reads. Total: " + totalUsersScanned;
    }

    @Override
    public Long getCouponStock(Long couponId) {
        String key = "coupon:" + couponId + ":stock";
        Object stock = redisService.get(key);
        if (stock != null) {
            return Long.valueOf(stock.toString());
        }
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        redisService.set(key, (long) coupon.getStock());
        return (long) coupon.getStock();
    }

    @Override
    @Transactional
    public String dbTransactionIntensive(Principal principal, Long couponId) {
        String userEmail = principal.getName();
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        coupon.decreaseStock();
        userCouponRepository.save(new UserCoupon(userEmail, couponId));
        return "Burned DB via Transaction.";
    }

    @Override
    @Transactional
    public String dbLockContention(Principal principal, Long couponId, long holdMs) throws InterruptedException {
        String userEmail = principal.getName();
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        Thread.sleep(holdMs);
        coupon.decreaseStock();
        userCouponRepository.save(new UserCoupon(userEmail, couponId));
        return "Lock contention test done.";
    }

    @Override
    public String dbTransactionRedis(Principal principal, Long couponId) {
        String userEmail = principal.getName();
        String key = "coupon:" + couponId + ":stock";
        Long remain = redisService.decrement(key);
        if (remain == null || remain < 0) {
            if (remain != null && remain < 0) {
                redisService.increment(key);
            }
            throw new BusinessException(ErrorType.OUT_OF_STOCK);
        }
        redisService.sAdd("coupon:sync:ids", couponId);
        return "Success: Decreased stock in Redis and queued issuance. Remain: " + remain;
    }
}
