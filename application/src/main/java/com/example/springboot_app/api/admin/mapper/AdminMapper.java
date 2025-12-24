package com.example.springboot_app.api.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.global.mapper.PagedMapper;

/**
 * Admin 도메인 관련 객체 간의 변환을 담당하는 매퍼입니다.
 * 주로 관리자용 응답 DTO 변환을 처리합니다.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdminMapper extends PagedMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "remainingQuantity", source = "remainingQuantity")
    CouponResponse.Stock toStockDto(Coupon coupon);

    default CouponResponse.Stock toStockDto(Long id, Integer remainingQuantity) {
        return new CouponResponse.Stock(id, remainingQuantity);
    }
}
