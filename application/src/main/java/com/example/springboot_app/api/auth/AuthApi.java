package com.example.springboot_app.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.lib.security.starter.annotation.AuthPublic;
import com.example.lib.security.starter.annotation.AuthSelf;
import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Login and Signup APIs")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @AuthPublic
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "User Signup", description = "Creates a new user account.")
    @PostMapping("/signup")
    AuthResponse.UserInfo signup(@RequestBody @Valid AuthRequest.Signup request);

    @AuthPublic
    @Operation(summary = "User Login", description = "Authenticates user and returns access token in body and refresh token in cookie.")
    @PostMapping("/login")
    AuthResponse.Token login(@RequestBody @Valid AuthRequest.Login request);

    @AuthSelf
    @Operation(summary = "Get My Info", description = "Returns current authenticated user information.")
    @GetMapping("/me")
    AuthResponse.UserInfo getMe();

    @AuthSelf
    @Operation(summary = "User Logout", description = "Invalidates the user session and clears cookies.")
    @PostMapping("/logout")
    void logout();

    @AuthSelf
    @Operation(summary = "Refresh Token", description = "Gets a new access token using the refresh token.")
    @PostMapping("/refresh")
    AuthResponse.Token refresh();
}
