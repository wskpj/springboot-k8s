package com.example.lib.security.starter.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

/**
 * 인증된 사용자 정보를 담는 DTO입니다.
 * Spring Security의 User 클래스를 상속받아 인증 시스템과 통합됩니다.
 */
@Getter
public class AuthUser extends User {
    private final Long id;
    private final String email;

    public AuthUser(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
        super(String.valueOf(id), "", authorities);
        this.id = id;
        this.email = email;
    }
}
