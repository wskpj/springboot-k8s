package com.example.lib.security.starter.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.lib.security.starter.aspect.AuthSelfAspect;
import com.example.lib.security.starter.bean.JwtProvider;
import com.example.lib.security.starter.handler.CustomAccessDeniedHandler;
import com.example.lib.security.starter.handler.CustomAuthenticationEntryPoint;
import com.example.lib.security.starter.handler.SecurityExceptionStrategy;
import com.example.lib.security.starter.resolver.AuthAnnotationResolver;
import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.bean.ApiGenerator;

import lombok.RequiredArgsConstructor;

/**
 * 시큐리티 스타터의 기본 설정을 담은 추상 클래스입니다.
 */
@RequiredArgsConstructor
public abstract class SecurityStarterConfig implements WebMvcConfigurer {

    private final HandlerMappingIntrospector introspector;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    @Bean
    public AuthSelfAspect authSelfAspect() {
        return new AuthSelfAspect();
    }

    @Bean
    public SecurityExceptionStrategy securityExceptionStrategy() {
        return new SecurityExceptionStrategy();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(ErrorDispatcher errorDispatcher, ApiGenerator apiGenerator) {
        return new CustomAccessDeniedHandler(errorDispatcher, apiGenerator);
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(ErrorDispatcher errorDispatcher, ApiGenerator apiGenerator) {
        return new CustomAuthenticationEntryPoint(errorDispatcher, apiGenerator);
    }

    @Bean
    public AuthAnnotationResolver authAnnotationResolver() {
        return new AuthAnnotationResolver(introspector);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // No longer needed as we use CurrentUser proxy injection
    }
}
