package com.example.springboot_app.domain.auth.dto;

import com.example.springboot_app.domain.user.entity.User;

public class AuthResult {
    public record UserInfo(
        User user,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn
    ) {}

    public record Token(
        String accessToken,
        String refreshToken
    ) {}
}
