package com.example.springboot_app.api.user.dto;

import java.time.OffsetDateTime;

public class UserResponse {

    public record UserInfo(
        Long id,
        String email,
        String name,
        OffsetDateTime createdAt
    ) {}
}
