package com.example.springboot_app.api.auth;

import com.example.springboot_app.api.auth.dto.AuthRequest;
import com.example.springboot_app.api.auth.dto.AuthResponse;
import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.service.AuthService;
import com.example.springboot_app.global.error.ErrorType;
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
    public ResponseEntity<ApiResult<Void>> signup(@RequestBody @Valid AuthRequest.Signup request) {
        authService.signup(request.toParam());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResult<AuthResponse.Token>> login(@RequestBody @Valid AuthRequest.Login request, HttpServletResponse response) {
        AuthResult.Token tokenDto = authService.login(request.toParam());

        // Set refresh token as http-only cookie
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        // cookie.setSecure(true); // Enable this in production with HTTPS
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResult.success(new AuthResponse.Token(tokenDto.accessToken())));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResult<AuthResponse.UserInfo>> getMe(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (principal == null) {
            throw new BaseException(ErrorType.UNAUTHORIZED);
        }
        String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.fromResult(authService.getUserInfo(principal.getName(), accessToken, refreshToken));
        return ResponseEntity.ok(ApiResult.success(userInfo));
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
    public ResponseEntity<ApiResult<AuthResponse.Token>> refresh(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
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

        return ResponseEntity.ok(ApiResult.success(new AuthResponse.Token(tokenDto.accessToken())));
    }
}
