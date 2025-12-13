package com.example.springboot_app.domain.test.entity;

import com.example.springboot_app.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Long couponId;

    public UserCoupon(String userEmail, Long couponId) {
        this.userEmail = userEmail;
        this.couponId = couponId;
    }
}
