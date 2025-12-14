package com.example.springboot_app.domain.user.entity;

import com.example.springboot_app.domain.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // 비즈니스 로직 - 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 비즈니스 로직 - 이름 변경
    public void changeName(String newName) {
        this.name = newName;
    }
}
