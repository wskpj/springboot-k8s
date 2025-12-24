package com.example.lib.web.starter.internal.config;


import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 웹 스타터의 공통 설정 프로퍼티
 * allowedOrigins는 필수이며, 나머지는 기본값이 적용됩니다.
 */
@Validated
@ConfigurationProperties(prefix = "web")
public record WebProperties(
    @NotEmpty(message = "Missing required environment variable 'web.allowed-origins'. Please check your deployment config.")
    List<String> allowedOrigins,
    
    @DefaultValue("GET,POST,PUT,PATCH,DELETE,OPTIONS")
    List<String> allowedMethods,
    
    @DefaultValue("*")
    List<String> allowedHeaders,
    
    @DefaultValue("true")
    Boolean allowCredentials,
    
    @DefaultValue("3600")
    Long maxAge,

    @DefaultValue("com.example")
    List<String> responseFilterPrefixes
) {}
