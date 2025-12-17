package com.example.springboot_app.domain.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.domain.auth.dto.AuthParam;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.auth.redis.AuthRedisRepository;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.entity.UserRole;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.exception.enums.BusinessError;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthRedisRepository authRedisRepository;

    @Value("${app.admin-emails}")
    private List<String> adminEmails;

    @Transactional
    public void signup(AuthParam.Signup param) {
        if (userRepository.existsByEmail(param.email())) {
            throw new BusinessException(BusinessError.EMAIL_ALREADY_EXISTS);
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
                .orElseThrow(() -> new BusinessException(BusinessError.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(param.password(), user.getPassword())) {
            throw new BusinessException(BusinessError.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().getAuthority());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // Save refresh token in Redis Set with TTL
        long ttl = jwtProvider.getExpirationRemainSeconds(refreshToken);
        authRedisRepository.addRefreshToken(user.getId(), refreshToken, ttl);

        return new AuthResult.Token(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResult.UserInfo getUserInfo(String email, String accessToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(BusinessError.USER_NOT_FOUND));
        
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
            throw new BusinessException(BusinessError.INVALID_TOKEN);
        }

        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        
        // Verify token presence in the Redis Set
        if (!authRedisRepository.isValidRefreshToken(userId, refreshToken)) {
            throw new BusinessException(BusinessError.INVALID_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessError.USER_NOT_FOUND));

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
