package com.example.springboot_app.api.auth;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.service.AuthService;
import com.example.springboot_app.global.error.ErrorType;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public void signup(@RequestBody @Valid AuthRequest.Signup request) {
        authService.signup(request.toParam());
    }

    @PostMapping("/login")
    public AuthResponse.Token login(@RequestBody @Valid AuthRequest.Login request, HttpServletResponse response) {
        AuthResult.Token tokenDto = authService.login(request.toParam());

        // Set refresh token as http-only cookie
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);

        return new AuthResponse.Token(tokenDto.accessToken());
    }

    @GetMapping("/me")
    public AuthResponse.UserInfo getMe(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (principal == null) {
            throw new BaseException(ErrorType.UNAUTHORIZED);
        }
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        return AuthResponse.UserInfo.from(authService.getUserInfo(principal.getName(), accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public void logout(Principal principal, HttpServletResponse response) {
        if (principal != null) {
            authService.logout(principal.getName());
        }
        
        // Clear cookie
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/refresh")
    public AuthResponse.Token refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }

        AuthResult.Token tokenDto = authService.refresh(refreshToken);

        // Set new refresh token
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); 
        response.addCookie(cookie);

        return new AuthResponse.Token(tokenDto.accessToken());
    }
}
