package com.example.springboot_app.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.lib.common.core.context.UserContext;
import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.api.auth.mapper.AuthMapper;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.exception.AuthException;
import com.example.springboot_app.domain.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(AuthRequest.Signup request) {
        authService.signup(authMapper.toSignupParam(request));
    }

    @Override
    public AuthResponse.Token login(AuthRequest.Login request, HttpServletResponse response) {
        AuthResult.Token tokenDto = authService.login(authMapper.toLoginParam(request));

        // Set refresh token as http-only cookie
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);

        return authMapper.toTokenResponse(tokenDto);
    }

    @Override
    public AuthResponse.UserInfo getMe(UserContext user, String authHeader, String refreshToken) {
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        return authMapper.toUserInfoResponse(authService.getUserInfo(user.name(), accessToken, refreshToken));
    }

    @Override
    public void logout(UserContext user, String refreshToken, HttpServletResponse response) {
        if (user != null) {
            authService.logout(Long.parseLong(user.userId()), refreshToken);
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
            throw new AuthException.InvalidToken();
        }

        AuthResult.Token tokenDto = authService.refresh(refreshToken);

        // Set new refresh token
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); 
        response.addCookie(cookie);

        return authMapper.toTokenResponse(tokenDto);
    }
}
