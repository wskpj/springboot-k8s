package com.example.springboot_app.api.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.springboot_app.api.admin.dto.AdminCouponRequest;
import com.example.springboot_app.api.coupon.dto.CouponResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Admin Coupon API", description = "관리자 전용 쿠폰 관리 API")
@RequestMapping("/api/v1/admin")
public interface AdminApi {

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 시스템에 등록합니다.")
    @PostMapping("/coupons/stock")
    @ResponseStatus(HttpStatus.CREATED)
    CouponResponse.Stock createCoupon(@Valid @RequestBody AdminCouponRequest.Create request);
}
