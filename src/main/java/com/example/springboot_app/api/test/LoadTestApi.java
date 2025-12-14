package com.example.springboot_app.api.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Load Test", description = "Load Test APIs")
@RequestMapping("/api/v1/stress")
public interface LoadTestApi {

    @Operation(summary = "CPU Stress (BCrypt)")
    @GetMapping("/cpu/bcrypt")
    String cpuIntensiveBcrypt(@RequestParam(defaultValue = "10") int rounds);

    @Operation(summary = "CPU Stress (Factorial)")
    @GetMapping("/cpu/factorial")
    String cpuIntensiveFactorial(@RequestParam(defaultValue = "10000") int n);

    @Operation(summary = "DB Write Stress")
    @GetMapping("/db/write")
    String dbWriteIntensive(@RequestParam(defaultValue = "1") int count);

    @Operation(summary = "DB Read Stress")
    @GetMapping("/db/read")
    String dbReadIntensive(@RequestParam(defaultValue = "1") int iterations);

    @Operation(summary = "Get Coupon Stock")
    @GetMapping("/coupon/stock")
    Long getCouponStock(@RequestParam(defaultValue = "1") Long couponId);

    @Operation(summary = "DB Transaction Stress")
    @GetMapping("/db/transaction")
    String dbTransactionIntensive(Principal principal, @RequestParam(defaultValue = "1") Long couponId);

    @Operation(summary = "DB Lock Contention Stress")
    @GetMapping("/db/lock-contention")
    String dbLockContention(
            Principal principal,
            @RequestParam(defaultValue = "1") Long couponId,
            @RequestParam(defaultValue = "0") long holdMs
    ) throws InterruptedException;

    @Operation(summary = "Redis Atomic Transaction Stress")
    @GetMapping("/db/transaction-redis")
    String dbTransactionRedis(Principal principal, @RequestParam(defaultValue = "1") Long couponId);
}
