package com.example.springboot_app.domain.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.auth.dto.AuthParam;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.exception.AuthException;
import com.example.springboot_app.domain.auth.redis.AuthRedisRepository;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.entity.UserRole;
import com.example.springboot_app.domain.user.exception.UserException;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.infrastructure.security.bean.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 1800L; // 30 minutes

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthRedisRepository authRedisRepository;

    @Value("${app.admin-emails}")
    private List<String> adminEmails;

    @Transactional
    public void signup(AuthParam.Signup param) {
        if (userRepository.existsByEmail(param.email())) {
            throw new AuthException.EmailAlreadyExists(param.email());
        }

        UserRole role = adminEmails.contains(param.email()) ? UserRole.ADMIN : UserRole.USER;

        User user = User.builder()
                .email(param.email())
                .password(passwordEncoder.encode(param.password()))
                .name(param.name())
                .role(role)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResult.Token login(AuthParam.Login param) {
        // 1. 계정 잠금 상태 확인
        int failCount = authRedisRepository.getLoginFailCount(param.email());
        if (failCount >= MAX_LOGIN_ATTEMPTS) {
            throw new AuthException.TooManyLoginAttempts();
        }

        User user = userRepository.findByEmail(param.email())
                .orElseThrow(() -> {
                    // 존재하지 않는 유저라도 보안상 실패 횟수 증가 (유저 존재 여부 유추 방지)
                    authRedisRepository.incrementLoginFailCount(param.email(), LOCKOUT_DURATION);
                    return new AuthException.InvalidCredentials();
                });

        if (!passwordEncoder.matches(param.password(), user.getPassword())) {
            // 2. 비밀번호 틀린 경우 실패 횟수 증가 및 잠금 처리
            long currentFailCount = authRedisRepository.incrementLoginFailCount(user.getEmail(), LOCKOUT_DURATION);
            if (currentFailCount >= MAX_LOGIN_ATTEMPTS) {
                throw new AuthException.TooManyLoginAttempts();
            }
            throw new AuthException.InvalidCredentials();
        }

        // 3. 로그인 성공 시 실패 횟수 초기화
        authRedisRepository.clearLoginFailCount(user.getEmail());

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().getAuthority());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // 4. Redis Set에 Refresh Token 저장
        long ttl = jwtProvider.getExpirationRemainSeconds(refreshToken);
        authRedisRepository.addRefreshToken(user.getId(), refreshToken, ttl);

        return new AuthResult.Token(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResult.UserInfo getUserInfo(String email, String accessToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException.NotFound());
        
        long atExpiresIn = jwtProvider.getExpirationRemainSeconds(accessToken);
        long rtExpiresIn = jwtProvider.getExpirationRemainSeconds(refreshToken);
        
        return new AuthResult.UserInfo(user, atExpiresIn, rtExpiresIn);
    }

    public void logout(Long userId, String refreshToken) {
        if (refreshToken != null) {
            authRedisRepository.removeRefreshToken(userId, refreshToken);
        }
    }

    public AuthResult.Token refresh(String refreshToken) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new AuthException.InvalidToken();
        }

        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        
        // Verify token presence in the Redis Set
        if (!authRedisRepository.isValidRefreshToken(userId, refreshToken)) {
            throw new AuthException.InvalidToken();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException.NotFound());

        // Issue new tokens
        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().getAuthority());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        // Replace old token with new one in the Set
        authRedisRepository.removeRefreshToken(userId, refreshToken);
        long newTtl = jwtProvider.getExpirationRemainSeconds(newRefreshToken);
        authRedisRepository.addRefreshToken(userId, newRefreshToken, newTtl);

        return new AuthResult.Token(newAccessToken, newRefreshToken);
    }
}
