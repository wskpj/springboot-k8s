package com.example.springboot_app.api.auth.dto;

import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.user.entity.User;

public class AuthResponse {

    public record Token(String accessToken) {}

    public record UserInfo(
        Long id,
        String email,
        String name,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn
    ) {
        public static UserInfo from(AuthResult.UserInfo result) {
            User user = result.user();
            return new UserInfo(
                user.getId(),
                user.getEmail(),
                user.getName(),
                result.accessTokenExpiresIn(),
                result.refreshTokenExpiresIn()
            );
        }
    }
}
