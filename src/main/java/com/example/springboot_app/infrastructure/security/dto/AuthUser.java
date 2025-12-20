package com.example.springboot_app.infrastructure.security.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

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
