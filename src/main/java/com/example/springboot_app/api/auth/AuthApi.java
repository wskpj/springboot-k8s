package com.example.springboot_app.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.domain.auth.annotations.AuthPublic;
import com.example.springboot_app.infrastructure.security.dto.AuthUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Login and Signup APIs")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(summary = "User Signup", description = "Creates a new user account.")
    @PostMapping("/signup")
    @AuthPublic
    @ResponseStatus(HttpStatus.CREATED)
    void signup(@RequestBody @Valid AuthRequest.Signup request);

    @Operation(summary = "User Login", description = "Authenticates user and returns access token in body and refresh token in cookie.")
    @PostMapping("/login")
    @AuthPublic
    AuthResponse.Token login(@RequestBody @Valid AuthRequest.Login request, HttpServletResponse response);

    @Operation(summary = "Get My Info", description = "Returns current authenticated user information.")
    @GetMapping("/me")
    AuthResponse.UserInfo getMe(
            AuthUser user,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshToken);

    @Operation(summary = "User Logout", description = "Invalidates the user session and clears cookies.")
    @PostMapping("/logout")
    void logout(
            AuthUser user,
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response);

    @Operation(summary = "Refresh Token", description = "Gets a new access token using the refresh token.")
    @PostMapping("/refresh")
    @AuthPublic
    AuthResponse.Token refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response);
}
