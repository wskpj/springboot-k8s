package com.example.springboot_app.api.coupon;

import org.springframework.web.bind.annotation.RestController;

import com.example.lib.common.core.context.user.UserContext;
import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.api.coupon.mapper.CouponMapper;
import com.example.springboot_app.domain.coupon.exception.CouponException;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final UserContext userContext;

    @Override
    public void issueCoupon(Long id) {
        couponService.issueCoupon(id);
    }

    @Override
    public CouponResponse.Stock getStock(Long id) {
        return couponRepository.findById(id)
                .map(couponMapper::toStockDto)
                .orElseThrow(() -> new CouponException.NotFound(id));
    }
}
