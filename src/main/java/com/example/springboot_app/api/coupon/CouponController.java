package com.example.springboot_app.api.coupon;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.domain.coupon.repository.CouponRepository;
import com.example.springboot_app.domain.coupon.service.CouponService;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(Long id, Principal principal) {
        if (principal == null) {
            throw new BusinessException(ErrorType.UNAUTHORIZED);
        }
        couponService.issueCoupon(id, principal.getName());
    }

    @Override
    public CouponResponse.Stock getStock(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.COUPON_NOT_FOUND));
        return new CouponResponse.Stock(coupon.getId(), coupon.getRemainingQuantity());
    }
}
