package com.example.springboot_app.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.service.AuthService;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.security.AuthUser;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(AuthRequest.Signup request) {
        authService.signup(request.toParam());
    }

    @Override
    public AuthResponse.Token login(AuthRequest.Login request, HttpServletResponse response) {
        AuthResult.Token tokenDto = authService.login(request.toParam());

        // Set refresh token as http-only cookie
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);

        return new AuthResponse.Token(tokenDto.accessToken());
    }

    @Override
    public AuthResponse.UserInfo getMe(AuthUser user, String authHeader, String refreshToken) {
        if (user == null) {
            throw new BusinessException(ErrorType.UNAUTHORIZED);
        }
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        return AuthResponse.UserInfo.from(authService.getUserInfo(user.getEmail(), accessToken, refreshToken));
    }

    @Override
    public void logout(AuthUser user, HttpServletResponse response) {
        if (user != null) {
            authService.logout(user.getId());
        }
        
        // Clear cookie
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public AuthResponse.Token refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new BusinessException(ErrorType.INVALID_TOKEN);
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
