package com.example.springboot_app.domain.test.repository;

import com.example.springboot_app.domain.test.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
