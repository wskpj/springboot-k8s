package com.example.springboot_app.infrastructure.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

/**
 * 애플리케이션 전용 비즈니스 프로퍼티
 */
@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
    @NotEmpty(message = "app.admin-emails 설정이 누락되었습니다.")
    List<String> adminEmails
) {}
