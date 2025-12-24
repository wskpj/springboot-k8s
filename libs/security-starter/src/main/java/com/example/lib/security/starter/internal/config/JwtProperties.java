package com.example.lib.security.starter.internal.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * JWT 관련 설정 프로퍼티
 * 환경변수: JWT_SECRET, JWT_ACCESS_TOKEN_VALIDITY_IN_SECONDS 등
 */
@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    @NotEmpty(message = "jwt.secret 설정이 누락되었습니다.")
    String secret,
    
    @DefaultValue("3600")
    @NotNull
    Long accessTokenValidityInSeconds,
    
    @DefaultValue("86400")
    @NotNull
    Long refreshTokenValidityInSeconds
) {}
