package com.example.springboot_app.domain.coupon.dto;

public class CouponParam {
    public record Issue(
        String title,
        Integer totalQuantity
    ) {}
}
