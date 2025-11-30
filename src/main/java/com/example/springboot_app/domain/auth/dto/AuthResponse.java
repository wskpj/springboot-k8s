package com.example.springboot_app.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    // Add other fields like user info if needed
}
