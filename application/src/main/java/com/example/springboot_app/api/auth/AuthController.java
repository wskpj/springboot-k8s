package com.example.springboot_app.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.web.core.cookie.CookieManager;
import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.api.auth.mapper.AuthMapper;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.exception.AuthException;
import com.example.springboot_app.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    private final AuthService authService;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final UserContext userContext;

    @Override
    public void signup(AuthRequest.Signup request) {
        authService.signup(authMapper.toSignupParam(request));
    }

    @Override
    public AuthResponse.Token login(AuthRequest.Login request) {
        AuthResult.Token tokenDto = authService.login(authMapper.toLoginParam(request));

        cookieManager.addCookie(REFRESH_TOKEN_COOKIE, tokenDto.refreshToken(), REFRESH_TOKEN_MAX_AGE);

        return authMapper.toTokenResponse(tokenDto);
    }

    @Override
    public AuthResponse.UserInfo getMe() {
        String refreshToken = cookieManager.getCookie(REFRESH_TOKEN_COOKIE)
                .orElseThrow(() -> new AuthException.InvalidToken());
        return authMapper.toUserInfoResponse(authService.getUserInfo(refreshToken));
    }

    @Override
    public void logout() {
        // userContext.user()는 항상 존재함을 신뢰할 수 있습니다.
        if (!userContext.user().isGuest()) {
            String refreshToken = cookieManager.getCookie(REFRESH_TOKEN_COOKIE)
                    .orElseThrow(() -> new AuthException.InvalidToken());
            authService.logout(userContext.user().userId(), refreshToken);
        }
        cookieManager.removeCookie(REFRESH_TOKEN_COOKIE);
    }

    @Override
    public AuthResponse.Token refresh() {
        String refreshToken = cookieManager.getCookie(REFRESH_TOKEN_COOKIE)
                .orElseThrow(() -> new AuthException.InvalidToken());
        AuthResult.Token tokenDto = authService.refresh(refreshToken);

        cookieManager.addCookie(REFRESH_TOKEN_COOKIE, tokenDto.refreshToken(), REFRESH_TOKEN_MAX_AGE);

        return authMapper.toTokenResponse(tokenDto);
    }
}
