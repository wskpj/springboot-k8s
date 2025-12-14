package com.example.springboot_app.api.auth;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Authentication", description = "Login and Signup APIs")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(summary = "User Signup", description = "Creates a new user account.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    void signup(@RequestBody @Valid AuthRequest.Signup request);

    @Operation(summary = "User Login", description = "Authenticates user and returns access token in body and refresh token in cookie.")
    @PostMapping("/login")
    AuthResponse.Token login(@RequestBody @Valid AuthRequest.Login request, HttpServletResponse response);

    @Operation(summary = "Get My Info", description = "Returns current authenticated user information.")
    @GetMapping("/me")
    AuthResponse.UserInfo getMe(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshToken);

    @Operation(summary = "User Logout", description = "Invalidates the user session and clears cookies.")
    @PostMapping("/logout")
    void logout(Principal principal, HttpServletResponse response);

    @Operation(summary = "Refresh Token", description = "Gets a new access token using the refresh token.")
    @PostMapping("/refresh")
    AuthResponse.Token refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response);
}
