package com.example.springboot_app.api.auth.dto;

import com.example.springboot_app.domain.auth.dto.AuthParam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    public record Signup(
        @NotBlank(message = "Email is required")
        @Email(message = "Format must be an email")
        @Schema(description = "이메일 주소", example = "user@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        @Schema(description = "비밀번호", example = "password1@Q")
        String password,

        @NotBlank(message = "Name is required")
        @Schema(description = "사용자 이름", example = "홍길동")
        String name
    ) {
        public AuthParam.Signup toParam() {
            return new AuthParam.Signup(email, password, name);
        }
    }

    public record Login(
        @Schema(description = "이메일 주소", example = "user@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Format must be an email")
        String email,

        @Schema(description = "비밀번호", example = "password1@Q")
        @NotBlank(message = "Password is required")
        String password
    ) {
        public AuthParam.Login toParam() {
            return new AuthParam.Login(email, password);
        }
    }
}
