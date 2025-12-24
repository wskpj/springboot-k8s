package com.example.springboot_app.api.admin;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_app.api.admin.dto.AdminCouponRequest;
import com.example.springboot_app.api.admin.mapper.AdminMapper;
import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Override
    public CouponResponse.Stock createCoupon(AdminCouponRequest.Create request) {
        var result = adminService.createCoupon(request.title(), request.totalQuantity());
        return adminMapper.toStockDto(result.id(), result.totalQuantity());
    }

    @Override
    public CouponResponse.Stock getCouponStock(Long id) {
        return adminMapper.toStockDto(id, adminService.getCouponStock(id));
    }

    @Override
    public void refreshStock(Long id) {
        adminService.refreshCouponStock(id);
    }

    @Override
    public void syncStock() {
        adminService.syncStockNow();
    }
}
