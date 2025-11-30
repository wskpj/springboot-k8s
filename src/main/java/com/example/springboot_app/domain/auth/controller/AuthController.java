package com.example.springboot_app.domain.auth.controller;

import com.example.springboot_app.common.dto.ApiResult;
import com.example.springboot_app.domain.auth.dto.AuthResponse;
import com.example.springboot_app.domain.auth.dto.LoginRequest;
import com.example.springboot_app.domain.auth.dto.SignupRequest;
import com.example.springboot_app.domain.auth.dto.TokenDto;
import com.example.springboot_app.domain.auth.service.AuthService;
import com.example.springboot_app.domain.auth.dto.UserInfoResponse;
import com.example.springboot_app.global.error.ErrorCode;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.security.Principal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResult<Void>> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResult<AuthResponse>> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(request);

        // Set refresh token as http-only cookie
        Cookie cookie = new Cookie("refresh_token", tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        // cookie.setSecure(true); // Enable this in production with HTTPS
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResult.success(new AuthResponse(tokenDto.getAccessToken())));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResult<UserInfoResponse>> getMe(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (principal == null) {
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        return ResponseEntity.ok(ApiResult.success(authService.getUserInfo(principal.getName(), accessToken, refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResult<Void>> logout(Principal principal, HttpServletResponse response) {
        if (principal != null) {
            authService.logout(principal.getName());
        }
        
        // Clear cookie
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<AuthResponse>> refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        TokenDto tokenDto = authService.refresh(refreshToken);

        // Set new refresh token
        Cookie cookie = new Cookie("refresh_token", tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); 
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResult.success(new AuthResponse(tokenDto.getAccessToken())));
    }
}
