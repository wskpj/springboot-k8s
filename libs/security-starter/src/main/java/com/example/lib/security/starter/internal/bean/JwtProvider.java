package com.example.lib.security.starter.internal.bean;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import com.example.lib.security.core.bean.TokenProvider;
import com.example.lib.security.starter.internal.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 생성 및 검증을 담당하는 컴포넌트입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtProvider implements TokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public String createAccessToken(Long userId, String email, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.accessTokenValidityInSeconds() * 1000);


        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.refreshTokenValidityInSeconds() * 1000);


        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    @Override
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("email", String.class);
    }

    @Override
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

    @Override
    public long getExpirationRemainSeconds(String token) {
        if (token == null || !validateToken(token)) return 0;
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        long expTime = claims.getExpiration().getTime();
        long now = System.currentTimeMillis();
        return Math.max(0, (expTime - now) / 1000);
    }
}
