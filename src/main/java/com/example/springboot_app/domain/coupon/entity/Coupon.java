package com.example.springboot_app.domain.coupon.entity;

import com.example.springboot_app.domain.common.entity.BaseEntity;
import com.example.springboot_app.global.enums.BusinessError;
import com.example.springboot_app.global.error.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Builder
    public Coupon(String title, Integer totalQuantity, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = totalQuantity;
    }

    /**
     * 발급 가능 여부 확인
     */
    public void validateIssuance() {
        if (remainingQuantity <= 0) {
            throw new BusinessException(BusinessError.OUT_OF_STOCK);
        }
    }

    /**
     * 재고 감소
     */
    public void decreaseRemainingQuantity() {
        if (this.remainingQuantity <= 0) {
            throw new BusinessException(BusinessError.OUT_OF_STOCK);
        }
        this.remainingQuantity--;
    }
}
