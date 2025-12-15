package com.example.springboot_app.api.admin;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_app.api.admin.dto.AdminCouponRequest;
import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.admin.service.AdminService;
import com.example.springboot_app.domain.coupon.entity.Coupon;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @Override
    public CouponResponse.Stock createCoupon(AdminCouponRequest.Create request) {
        Coupon coupon = adminService.createCoupon(request.title(), request.totalQuantity());
        return new CouponResponse.Stock(coupon.getId(), coupon.getRemainingQuantity());
    }
}
