package com.example.springboot_app.domain.test.controller;

import com.example.springboot_app.common.dto.ApiResult;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.test.entity.Coupon;
import com.example.springboot_app.domain.test.entity.UserCoupon;
import com.example.springboot_app.domain.test.repository.CouponRepository;
import com.example.springboot_app.domain.test.repository.UserCouponRepository;
import com.example.springboot_app.global.service.RedisService;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/stress")
@RequiredArgsConstructor
@Tag(name = "Load Test", description = "Load Test APIs")
public class LoadTestController {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisService redisService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Operation(summary = "CPU Stress (BCrypt)")
    @GetMapping("/cpu/bcrypt")
    public ApiResult<String> cpuIntensiveBcrypt(@RequestParam(defaultValue = "10") int rounds) {
        long start = System.currentTimeMillis();
        String result = "load-test-seed";
        for (int i = 0; i < rounds; i++) {
            result = encoder.encode(result);
        }
        long elapsed = System.currentTimeMillis() - start;
        return ApiResult.success("Burned CPU using BCrypt. Elapsed: " + elapsed + "ms");
    }

    @Operation(summary = "CPU Stress (Factorial)")
    @GetMapping("/cpu/factorial")
    public ApiResult<String> cpuIntensiveFactorial(@RequestParam(defaultValue = "10000") int n) {
        long start = System.currentTimeMillis();
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        long elapsed = System.currentTimeMillis() - start;
        return ApiResult.success("Burned CPU using Factorial. Elapsed: " + elapsed + "ms");
    }

    @Operation(summary = "DB Write Stress")
    @GetMapping("/db/write")
    @Transactional
    public ApiResult<String> dbWriteIntensive(@RequestParam(defaultValue = "1") int count) {
        for (int i = 0; i < count; i++) {
            String uuid = UUID.randomUUID().toString();
            User dummyUser = User.builder()
                    .email("stress_" + uuid + "@test.com")
                    .password("dummy")
                    .name("StressTestUser")
                    .build();
            userRepository.save(dummyUser);
        }
        return ApiResult.success("Burned DB via Writes.");
    }

    @Operation(summary = "DB Read Stress")
    @GetMapping("/db/read")
    @Transactional(readOnly = true)
    public ApiResult<String> dbReadIntensive(@RequestParam(defaultValue = "1") int iterations) {
        long totalUsersScanned = 0;
        for (int i = 0; i < iterations; i++) {
            totalUsersScanned += userRepository.count();
        }
        return ApiResult.success("Burned DB via Reads. Total: " + totalUsersScanned);
    }

    @Operation(summary = "Get Coupon Stock")
    @GetMapping("/coupon/stock")
    public ApiResult<Long> getCouponStock(@RequestParam(defaultValue = "1") Long couponId) {
        String key = "coupon:" + couponId + ":stock";
        Object stock = redisService.get(key);
        if (stock != null) {
            return ApiResult.success(Long.valueOf(stock.toString()));
        }
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        redisService.set(key, (long) coupon.getStock());
        return ApiResult.success((long) coupon.getStock());
    }

    @Operation(summary = "DB Transaction Stress")
    @GetMapping("/db/transaction")
    @Transactional
    public ApiResult<String> dbTransactionIntensive(Principal principal, @RequestParam(defaultValue = "1") Long couponId) {
        String userEmail = principal.getName();
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        coupon.decreaseStock();
        userCouponRepository.save(new UserCoupon(userEmail, couponId));
        return ApiResult.success("Burned DB via Transaction.");
    }

    @Operation(summary = "DB Lock Contention Stress")
    @GetMapping("/db/lock-contention")
    @Transactional
    public ApiResult<String> dbLockContention(
            Principal principal,
            @RequestParam(defaultValue = "1") Long couponId,
            @RequestParam(defaultValue = "0") long holdMs
    ) throws InterruptedException {
        String userEmail = principal.getName();
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        Thread.sleep(holdMs);
        coupon.decreaseStock();
        userCouponRepository.save(new UserCoupon(userEmail, couponId));
        return ApiResult.success("Lock contention test done.");
    }

    @Operation(summary = "Redis Atomic Transaction Stress")
    @GetMapping("/db/transaction-redis")
    public ApiResult<String> dbTransactionRedis(Principal principal, @RequestParam(defaultValue = "1") Long couponId) {
        String userEmail = principal.getName();
        String key = "coupon:" + couponId + ":stock";
        Long remain = redisService.decrement(key);
        if (remain == null || remain < 0) {
            if (remain != null && remain < 0) {
                redisService.increment(key);
            }
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }
        redisService.sAdd("coupon:sync:ids", couponId);
        // redisService.lPush("coupon:issuance:queue", userEmail + ":" + couponId);
        return ApiResult.success("Success: Decreased stock in Redis and queued issuance. Remain: " + remain);
    }
}
