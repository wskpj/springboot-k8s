package com.example.springboot_app.domain.test.repository;

import com.example.springboot_app.domain.test.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
