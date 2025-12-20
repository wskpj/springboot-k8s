package com.example.springboot_app.domain.auth.redis;

public interface AuthRedisRepository {

    void addRefreshToken(Long userId, String refreshToken, long ttlSeconds);

    boolean isValidRefreshToken(Long userId, String refreshToken);

    void removeRefreshToken(Long userId, String refreshToken);

    void removeAllRefreshTokens(Long userId);

    Long incrementLoginFailCount(String email, long lockDurationSeconds);

    int getLoginFailCount(String email);

    void clearLoginFailCount(String email);
}
