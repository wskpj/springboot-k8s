package com.example.springboot_app.api.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.springboot_app.api.admin.dto.AdminCouponRequest;
import com.example.springboot_app.api.coupon.dto.CouponResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.example.lib.security.core.annotation.AuthAdmin;


@Tag(name = "Admin Coupon API", description = "관리자 전용 쿠폰 관리 API")
@RequestMapping("/api/v1/admin")
@AuthAdmin
public interface AdminApi {

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 시스템에 등록합니다.")
    @PostMapping("/coupons")
    @ResponseStatus(HttpStatus.CREATED)
    CouponResponse.Stock createCoupon(@Valid @RequestBody AdminCouponRequest.Create request);

    @Operation(summary = "실시간 재고 조회", description = "Redis에 저장된 현재 재고 값을 즉시 조회합니다.")
    @GetMapping("/coupons/{id}/stock")
    CouponResponse.Stock getCouponStock(@PathVariable Long id);

    @Operation(summary = "쿠폰 재고 강제 웜업", description = "DB의 최신 재고 값을 Redis에 강제로 덮어씌웁니다.")
    @PostMapping("/coupons/{id}/refresh")
    void refreshStock(@PathVariable Long id);

    @Operation(summary = "쿠폰 재고 즉시 동기화", description = "Redis에 쌓인 재고 변경 내역을 즉시 DB에 반영합니다.")
    @PostMapping("/coupons/sync")
    void syncStock();
}
