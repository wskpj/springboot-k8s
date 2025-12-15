package com.example.springboot_app.api.coupon;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.service.CouponService;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(Long id, AuthUser user) {
        if (user == null) {
            throw new BusinessException(ErrorType.UNAUTHORIZED);
        }
        couponService.issueCoupon(id, user.getEmail());
    }

    @Override
    public CouponResponse.Stock getStock(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        return new CouponResponse.Stock(coupon.getId(), coupon.getRemainingQuantity());
    }
}
