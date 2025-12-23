package com.example.springboot_app.api.coupon.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.springboot_app.api.coupon.dto.CouponResponse;
import com.example.springboot_app.domain.coupon.entity.Coupon;
import com.example.springboot_app.global.mapper.PagedMapper;

/**
 * Coupon 도메인 관련 객체 간의 변환을 담당하는 매퍼입니다.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CouponMapper extends PagedMapper {

    /**
     * Coupon 엔티티를 Stock 응답 DTO로 변환합니다.
     */
    CouponResponse.Stock toStockDto(Coupon coupon);
}
