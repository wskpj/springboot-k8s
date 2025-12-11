package com.example.springboot_app.domain.auth.service;

import com.example.springboot_app.domain.auth.dto.LoginRequest;
import com.example.springboot_app.domain.auth.dto.SignupRequest;
import com.example.springboot_app.domain.auth.dto.TokenDto;
import com.example.springboot_app.domain.auth.dto.UserInfoResponse;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.error.ErrorCode;
import com.example.springboot_app.global.error.exception.BaseException;
import com.example.springboot_app.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot_app.global.service.RedisService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Refresh Token storage using Redis
    private final RedisService redisService;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // Save refresh token in Redis with TTL equal to token expiration
        long ttl = jwtProvider.getExpirationRemainSeconds(refreshToken);
        redisService.set("refresh:" + user.getEmail(), refreshToken, ttl);

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String email, String accessToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        
        long atExpiresIn = jwtProvider.getExpirationRemainSeconds(accessToken);
        long rtExpiresIn = jwtProvider.getExpirationRemainSeconds(refreshToken);
        
        return new UserInfoResponse(user, atExpiresIn, rtExpiresIn);
    }

    public void logout(String email) {
        redisService.delete("refresh:" + email);
    }

    public TokenDto refresh(String refreshToken) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtProvider.getEmailFromToken(refreshToken);
        String savedToken = (String) redisService.get("refresh:" + email);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        // Issue new tokens
        String newAccessToken = jwtProvider.createAccessToken(email);
        String newRefreshToken = jwtProvider.createRefreshToken(email);

        long newTtl = jwtProvider.getExpirationRemainSeconds(newRefreshToken);
        redisService.set("refresh:" + email, newRefreshToken, newTtl);

        return new TokenDto(newAccessToken, newRefreshToken);
    }
}
