package com.example.springboot_app.domain.user.entity;

import com.example.lib.jpa.core.entity.BaseEntity;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role != null ? role : UserRole.USER;
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
