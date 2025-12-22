package com.example.springboot_app.api.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouponResponse {

    public record Stock(
        @Schema(description = "쿠폰 ID", example = "1")
        Long id,
        @Schema(description = "잔여 수량", example = "45")
        Integer remainingQuantity
    ) {}
}
