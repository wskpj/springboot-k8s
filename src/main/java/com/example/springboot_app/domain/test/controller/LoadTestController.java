package com.example.springboot_app.domain.test.controller;

import com.example.springboot_app.common.dto.ApiResult;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.test.entity.Coupon;
import com.example.springboot_app.domain.test.entity.UserCoupon;
import com.example.springboot_app.domain.test.repository.CouponRepository;
import com.example.springboot_app.domain.test.repository.UserCouponRepository;
import com.example.springboot_app.global.service.RedisService;
import com.example.springboot_app.domain.user.repository.UserRepository;
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
@Tag(name = "Load Test", description = "부하 테스트를 위한 고비용 API")
public class LoadTestController {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisService redisService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Operation(summary = "CPU 부하 테스트 (BCrypt 암호화)", description = "주어진 횟수만큼 BCrypt 암호화를 반복하여 서버 측 CPU 부하를 발생시키는 부하 테스트")
    @GetMapping("/cpu/bcrypt")
    public ApiResult<String> cpuIntensiveBcrypt(@RequestParam(defaultValue = "10") int rounds) {
        long start = System.currentTimeMillis();
        
        String result = "load-test-seed";
        for (int i = 0; i < rounds; i++) {
            result = encoder.encode(result);
        }
        
        long elapsed = System.currentTimeMillis() - start;
        log.info("BCrypt Load Test: rounds={}, elapsed {} ms", rounds, elapsed);
        
        return ApiResult.success("Burned CPU using BCrypt. Rounds: " + rounds + ", Elapsed Time: " + elapsed + "ms");
    }

    @Operation(summary = "CPU 부하 테스트 (팩토리얼 연산)", description = "매우 큰 수의 팩토리얼을 계산하여 서버 측 CPU 부하를 발생시키는 부하 테스트")
    @GetMapping("/cpu/factorial")
    public ApiResult<String> cpuIntensiveFactorial(@RequestParam(defaultValue = "10000") int n) {
        long start = System.currentTimeMillis();
        
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        
        long elapsed = System.currentTimeMillis() - start;
        log.info("Factorial Load Test: n={}, elapsed {} ms", n, elapsed);
        
        return ApiResult.success("Burned CPU using Factorial. N: " + n + ", Elapsed Time: " + elapsed + "ms, Digits roughly " + result.toString().length());
    }

    @Operation(summary = "DB 쓰기 부하 테스트 (Insert)", description = "랜덤한 데이터를 지정된 횟수만큼 DB에 Insert하여 쓰기 지연 및 디스크 I/O 부하를 발생시키는 부하 테스트")
    @GetMapping("/db/write")
    @Transactional
    public ApiResult<String> dbWriteIntensive(@RequestParam(defaultValue = "1") int count) {
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < count; i++) {
            String uuid = UUID.randomUUID().toString();
            User dummyUser = User.builder()
                    .email("stress_" + uuid + "@test.com")
                    .password("dummy_password_no_encoding") // DB 순수 부하를 위해 암호화(CPU 연산) 생략
                    .name("StressTestUser")
                    .build();
            userRepository.save(dummyUser);
        }
        
        long elapsed = System.currentTimeMillis() - start;
        log.info("DB Write Load Test: count={}, elapsed {} ms", count, elapsed);
        
        return ApiResult.success("Burned DB via Writes. Inserted Rows: " + count + ", Elapsed Time: " + elapsed + "ms");
    }

    @Operation(summary = "DB 읽기 부하 테스트 (Count/Scan)", description = "모든 회원을 집계(Count)하는 쿼리를 여러 번 반복하여 DB 커넥션 풀과 읽기 속도(Table Scan)를 소모하는 부하 테스트")
    @GetMapping("/db/read")
    @Transactional(readOnly = true)
    public ApiResult<String> dbReadIntensive(@RequestParam(defaultValue = "1") int iterations) {
        long start = System.currentTimeMillis();
        
        long totalUsersScanned = 0;
        // DB에 직접 count 쿼리를 iterations 횟수만큼 반복 요청
        for (int i = 0; i < iterations; i++) {
            totalUsersScanned += userRepository.count();
        }
        
        long elapsed = System.currentTimeMillis() - start;
        log.info("DB Read Load Test: iterations={}, scanned total sum={}, elapsed {} ms", iterations, totalUsersScanned, elapsed);
        
        return ApiResult.success("Burned DB via Reads. Iterations: " + iterations + ", Total Evaluated Count Iterations Sum: " + totalUsersScanned + ", Elapsed Time: " + elapsed + "ms");
    }

    @Operation(summary = "쿠폰 재고 조회", description = "특정 쿠폰의 현재 재고 수량을 조회합니다. Redis를 먼저 확인하고 없으면 DB에서 로드합니다.")
    @GetMapping("/coupon/stock")
    public ApiResult<Long> getCouponStock(@RequestParam(defaultValue = "1") Long couponId) {
        String key = "coupon:" + couponId + ":stock";
        Object stock = redisService.get(key);
        
        if (stock != null) {
            return ApiResult.success(Long.valueOf(stock.toString()));
        }

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: id=" + couponId));
        
        // Redis에 캐싱
        redisService.set(key, (long) coupon.getStock());
        return ApiResult.success((long) coupon.getStock());
    }

    @Operation(summary = "DB 트랜잭션 부하 테스트 (쿠폰 발급 시나리오)", description = "JWT Decode, 쿠폰 재고 감소(Update) 및 발급 이력 기록(Insert)을 트랜잭션으로 처리하는 부하 시나리오")
    @GetMapping("/db/transaction")
    @Transactional
    public ApiResult<String> dbTransactionIntensive(Principal principal, @RequestParam(defaultValue = "1") Long couponId) {
        long start = System.currentTimeMillis();
        
        String userEmail = principal.getName();
        
        // 재고 조회
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
            
        // 재고 감소 (업데이트)
        coupon.decreaseStock();
        
        // 발급 이력 기록 (쓰기)
        UserCoupon userCoupon = new UserCoupon(userEmail, couponId);
        userCouponRepository.save(userCoupon);
        
        long elapsed = System.currentTimeMillis() - start;
        log.info("DB Transaction Load Test (Coupon): email={}, couponId={}, elapsed {} ms", userEmail, couponId, elapsed);
        
        return ApiResult.success("Burned DB via Transaction. Decreased stock & Inserted UserCoupon. Elapsed Time: " + elapsed + "ms");
    }

    @Operation(
        summary = "DB 락 경합 부하 테스트 (비관적 락)"
    )
    @GetMapping("/db/lock-contention")
    @Transactional
    public ApiResult<String> dbLockContention(
            Principal principal,
            @RequestParam(defaultValue = "1") Long couponId,
            @RequestParam(defaultValue = "0") long holdMs
    ) throws InterruptedException {
        long start = System.currentTimeMillis();
        String userEmail = principal.getName();

        // 비관적 락 획득 (SELECT ... FOR UPDATE)
        // 이 시점부터 동일 couponId 행에 대해 다른 트랜잭션은 대기 상태가 됩니다.
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: id=" + couponId));

        long lockAcquired = System.currentTimeMillis();
        log.info("Lock acquired: email={}, couponId={}, waitForLock={} ms", userEmail, couponId, lockAcquired - start);

        // holdMs 동안 인위적으로 락을 보유 → 동시 요청 시 나머지 스레드가 이 시간만큼 대기
        Thread.sleep(holdMs);

        // 락 보유 상태에서 재고 감소 및 이력 기록
        coupon.decreaseStock();
        userCouponRepository.save(new UserCoupon(userEmail, couponId));

        long elapsed = System.currentTimeMillis() - start;
        log.info("Lock released: email={}, couponId={}, holdMs={}, totalElapsed={} ms", userEmail, couponId, holdMs, elapsed);

        return ApiResult.success(String.format(
                "Lock contention test done. couponId=%d, holdMs=%d, totalElapsed=%d ms, remainingStock=%d",
                couponId, holdMs, elapsed, coupon.getStock()
        ));
    }

    @Operation(summary = "Redis 원자적 트랜잭션 부하 테스트", description = "Redis DECR을 이용한 원자적 재고 감소 후 DB에 이력 기록")
    @GetMapping("/db/transaction-redis")
    public ApiResult<String> dbTransactionRedis(Principal principal, @RequestParam(defaultValue = "1") Long couponId) {
        long start = System.currentTimeMillis();
        String userEmail = principal.getName();
        String key = "coupon:" + couponId + ":stock";

        // 1. Redis에서 원자적으로 감소
        Long remain = redisService.decrement(key);

        if (remain == null || remain < 0) {
            if (remain != null && remain < 0) {
                redisService.increment(key);
            }
            throw new IllegalArgumentException("Out of stock or Redis not warmed up: couponId=" + couponId);
        }

        // 2. 동기화 대기 목록(Set)에 추가
        redisService.sAdd("coupon:sync:ids", couponId);

        // 3. DB에는 발급 이력만 기록 (재고 차감은 Redis가 담당)
        userCouponRepository.save(new UserCoupon(userEmail, couponId));

        long elapsed = System.currentTimeMillis() - start;
        log.info("Redis Atomic Transaction: email={}, couponId={}, remain={}, elapsed {} ms", userEmail, couponId, remain, elapsed);

        return ApiResult.success("Success: Decreased stock in Redis & Saved UserCoupon in DB. Remain: " + remain);
    }
}
