package com.example.springboot_app.api.coupon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.auth.annotations.AuthPublic;
import com.example.springboot_app.infrastructure.security.dto.AuthUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Coupon", description = "Coupon Management APIs")
@RequestMapping("/api/v1/coupons")
public interface CouponApi {

    @Operation(summary = "Issue Coupon", description = "Issues a coupon to the authenticated user using high-concurrency Redis logic.")
    @PostMapping("/{id}/issue")
    void issueCoupon(@PathVariable Long id, AuthUser user);

    @Operation(summary = "Get Coupon Stock", description = "Returns the current remaining stock of a specific coupon.")
    @GetMapping("/{id}/stock")
    @AuthPublic
    CouponResponse.Stock getStock(@PathVariable Long id);
}
