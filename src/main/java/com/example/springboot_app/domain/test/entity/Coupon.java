package com.example.springboot_app.domain.test.entity;

import com.example.springboot_app.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stock;

    public Coupon(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    // 재고 감소 로직
    public void decreaseStock() {
        this.stock = this.stock - 1;
    }
}
