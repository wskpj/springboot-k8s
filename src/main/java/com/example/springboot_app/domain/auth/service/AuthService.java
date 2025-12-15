package com.example.springboot_app.domain.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.auth.dto.AuthParam;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.entity.UserRole;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.security.JwtProvider;
import com.example.springboot_app.global.service.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Refresh Token storage using Redis
    private final RedisService redisService;

    @Value("${app.admin-emails}")
    private List<String> adminEmails;

    @Transactional
    public void signup(AuthParam.Signup param) {
        if (userRepository.existsByEmail(param.email())) {
            throw new BusinessException(ErrorType.EMAIL_ALREADY_EXISTS);
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
        User user = userRepository.findByEmail(param.email())
                .orElseThrow(() -> new BusinessException(ErrorType.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(param.password(), user.getPassword())) {
            throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().getAuthority());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // Save refresh token in Redis with TTL equal to token expiration
        long ttl = jwtProvider.getExpirationRemainSeconds(refreshToken);
        redisService.set("refresh:" + user.getEmail(), refreshToken, ttl);

        return new AuthResult.Token(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResult.UserInfo getUserInfo(String email, String accessToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));
        
        long atExpiresIn = jwtProvider.getExpirationRemainSeconds(accessToken);
        long rtExpiresIn = jwtProvider.getExpirationRemainSeconds(refreshToken);
        
        return new AuthResult.UserInfo(user, atExpiresIn, rtExpiresIn);
    }

    public void logout(String email) {
        redisService.delete("refresh:" + email);
    }

    public AuthResult.Token refresh(String refreshToken) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorType.INVALID_TOKEN);
        }

        String email = jwtProvider.getEmailFromToken(refreshToken);
        String savedToken = (String) redisService.get("refresh:" + email);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BusinessException(ErrorType.INVALID_TOKEN);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        // Issue new tokens with role
        String newAccessToken = jwtProvider.createAccessToken(email, user.getRole().getAuthority());
        String newRefreshToken = jwtProvider.createRefreshToken(email);

        long newTtl = jwtProvider.getExpirationRemainSeconds(newRefreshToken);
        redisService.set("refresh:" + email, newRefreshToken, newTtl);

        return new AuthResult.Token(newAccessToken, newRefreshToken);
    }
}
