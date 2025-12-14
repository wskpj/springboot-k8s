package com.example.springboot_app.domain.auth.service;

import com.example.springboot_app.domain.auth.dto.AuthParam;
import com.example.springboot_app.domain.auth.dto.AuthResult;
import com.example.springboot_app.domain.user.entity.User;
import com.example.springboot_app.domain.user.repository.UserRepository;
import com.example.springboot_app.global.enums.ErrorType;
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
    public void signup(AuthParam.Signup param) {
        if (userRepository.existsByEmail(param.email())) {
            throw new BaseException(ErrorType.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(param.email())
                .password(passwordEncoder.encode(param.password()))
                .name(param.name())
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResult.Token login(AuthParam.Login param) {
        User user = userRepository.findByEmail(param.email())
                .orElseThrow(() -> new BaseException(ErrorType.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(param.password(), user.getPassword())) {
            throw new BaseException(ErrorType.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // Save refresh token in Redis with TTL equal to token expiration
        long ttl = jwtProvider.getExpirationRemainSeconds(refreshToken);
        redisService.set("refresh:" + user.getEmail(), refreshToken, ttl);

        return new AuthResult.Token(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResult.UserInfo getUserInfo(String email, String accessToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));
        
        long atExpiresIn = jwtProvider.getExpirationRemainSeconds(accessToken);
        long rtExpiresIn = jwtProvider.getExpirationRemainSeconds(refreshToken);
        
        return new AuthResult.UserInfo(user, atExpiresIn, rtExpiresIn);
    }

    public void logout(String email) {
        redisService.delete("refresh:" + email);
    }

    public AuthResult.Token refresh(String refreshToken) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }

        String email = jwtProvider.getEmailFromToken(refreshToken);
        String savedToken = (String) redisService.get("refresh:" + email);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BaseException(ErrorType.INVALID_TOKEN);
        }

        // Issue new tokens
        String newAccessToken = jwtProvider.createAccessToken(email);
        String newRefreshToken = jwtProvider.createRefreshToken(email);

        long newTtl = jwtProvider.getExpirationRemainSeconds(newRefreshToken);
        redisService.set("refresh:" + email, newRefreshToken, newTtl);

        return new AuthResult.Token(newAccessToken, newRefreshToken);
    }
}
