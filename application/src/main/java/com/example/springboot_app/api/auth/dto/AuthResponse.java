package com.example.springboot_app.api.auth.dto;

public class AuthResponse {

    public record Token(String accessToken) {}

    public record UserInfo(
        Long id,
        String email,
        String name,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn
    ) {}
}
