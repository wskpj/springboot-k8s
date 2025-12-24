package com.example.lib.security.core.bean;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenProvider {
    String createAccessToken(Long userId, String email, String role);
    String createRefreshToken(Long userId);
    String resolveToken(HttpServletRequest request);
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
    long getExpirationRemainSeconds(String token);
}
