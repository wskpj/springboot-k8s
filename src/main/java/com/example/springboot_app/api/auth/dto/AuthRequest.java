package com.example.springboot_app.api.auth.dto;

import com.example.springboot_app.domain.auth.dto.AuthParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    public record Signup(
        @NotBlank(message = "Email is required") @Email(message = "Format must be an email") String email,
        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
        @NotBlank(message = "Name is required") String name
    ) {
        public AuthParam.Signup toParam() {
            return new AuthParam.Signup(email, password, name);
        }
    }

    public record Login(
        @NotBlank(message = "Email is required") @Email(message = "Format must be an email") String email,
        @NotBlank(message = "Password is required") String password
    ) {
        public AuthParam.Login toParam() {
            return new AuthParam.Login(email, password);
        }
    }
}
