package com.example.springboot_app.domain.coupon.dto;

public record CouponResult() {
    public record Created(
        Long id,
        String title,
        Integer totalQuantity
    ) {
        public static Created from(com.example.springboot_app.domain.coupon.entity.Coupon coupon) {
            return new Created(coupon.getId(), coupon.getTitle(), coupon.getTotalQuantity());
        }
    }

    public static Created from(com.example.springboot_app.domain.coupon.entity.Coupon coupon) {
        return Created.from(coupon);
    }
}
