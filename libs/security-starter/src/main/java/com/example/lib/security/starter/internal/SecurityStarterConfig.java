package com.example.lib.security.starter.internal;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.security.core.bean.TokenProvider;
import com.example.lib.security.core.resolver.AuthResolver;
import com.example.lib.security.starter.internal.aspect.AuthAdminAspect;
import com.example.lib.security.starter.internal.aspect.AuthSelfAspect;

import com.example.lib.security.starter.internal.bean.JwtProvider;
import com.example.lib.security.starter.internal.config.JwtProperties;
import com.example.lib.security.starter.internal.config.SecurityProperties;
import com.example.lib.security.starter.internal.filter.JwtAuthenticationFilter;
import com.example.lib.security.starter.internal.handler.SecurityExceptionStrategy;
import com.example.lib.security.starter.internal.handler.StandardAccessDeniedHandler;
import com.example.lib.security.starter.internal.handler.StandardAuthenticationEntryPoint;
import com.example.lib.security.starter.internal.handler.SecurityExceptionStrategy;
import com.example.lib.security.starter.internal.resolver.AuthAnnotationResolver;
import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * 시큐리티 스타터의 설정을 자동으로 적용하는 설정 클래스입니다.
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, SecurityProperties.class})
public class SecurityStarterConfig implements WebMvcConfigurer {

    private final ObjectProvider<HandlerMappingIntrospector> introspectorProvider;

    @Bean
    @ConditionalOnMissingBean(AuthSelfAspect.class)
    public AuthSelfAspect authSelfAspect() {
        return new AuthSelfAspect();
    }

    @Bean
    @ConditionalOnMissingBean(AuthAdminAspect.class)
    public AuthAdminAspect authAdminAspect() {
        return new AuthAdminAspect();
    }

    @Bean
    @ConditionalOnMissingBean(name = "securityExceptionStrategy")
    public ExceptionHandleStrategy<Exception> securityExceptionStrategy() {
        return new SecurityExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler(ApiResultDispatcher dispatcher, ObjectMapper objectMapper) {
        return new StandardAccessDeniedHandler(dispatcher, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(ApiResultDispatcher dispatcher, ObjectMapper objectMapper) {
        return new StandardAuthenticationEntryPoint(dispatcher, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(AuthResolver.class)
    public AuthResolver authResolver() {
        return new AuthAnnotationResolver(introspectorProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(TokenProvider.class)
    public TokenProvider tokenProvider(JwtProperties jwtProperties) {
        return new JwtProvider(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "jwtAuthenticationFilter")
    public OncePerRequestFilter jwtAuthenticationFilter(TokenProvider tokenProvider) {
        return new JwtAuthenticationFilter((JwtProvider) tokenProvider);
    }

    @Bean
    @ConditionalOnMissingBean(name = "userContext")
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserContext userContext() {
        return UserContextHolder.getContext();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // No longer needed
    }
}
