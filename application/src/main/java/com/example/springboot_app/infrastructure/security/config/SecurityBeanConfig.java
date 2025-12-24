package com.example.springboot_app.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.lib.security.starter.bean.JwtProvider;

/**
 * 시큐리티 관련 인프라 빈(Provider, Encoder 등)을 정의하는 클래스입니다.
 * SecurityConfig와의 순환 참조를 방지하기 위해 분리되었습니다.
 */
@Configuration
public class SecurityBeanConfig {

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
