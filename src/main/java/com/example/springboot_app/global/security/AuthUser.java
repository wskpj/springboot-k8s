package com.example.springboot_app.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

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
