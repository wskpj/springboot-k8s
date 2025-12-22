package com.example.springboot_app.api.user.dto;

import java.time.LocalDateTime;

import com.example.springboot_app.domain.user.entity.User;

public class UserResponse {

    public record UserInfo(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt
    ) {
        public static UserInfo from(User user) {
            return new UserInfo(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt()
            );
        }
    }
}
