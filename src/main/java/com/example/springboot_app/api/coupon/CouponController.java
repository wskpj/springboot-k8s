package com.example.springboot_app.api.coupon;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.service.CouponService;
import com.example.springboot_app.global.exception.enums.BusinessError;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(Long id, AuthUser user) {
        couponService.issueCoupon(id, user.getId());
    }

    @Override
    public CouponResponse.Stock getStock(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessError.COUPON_NOT_FOUND));
        return new CouponResponse.Stock(coupon.getId(), coupon.getRemainingQuantity());
    }
}
