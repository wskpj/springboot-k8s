package com.example.lib.web.starter.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.lib.common.core.context.trace.CurrentTrace;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.common.core.context.user.CurrentUser;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.core.strategy.DefaultExceptionStrategy;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.starter.bean.ApiGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * 웹 스타터의 기본 설정을 담은 추상 클래스입니다.
 * WebMvcConfigurer를 구현하여 기본 웹 설정을 제공합니다.
 */
@RequiredArgsConstructor
public abstract class WebStarterConfig implements WebMvcConfigurer {

    @Bean
    public ApiGenerator apiGenerator(ObjectMapper objectMapper) {
        return new ApiGenerator(objectMapper);
    }

    @Bean
    public DefaultExceptionStrategy defaultExceptionStrategy() {
        return new DefaultExceptionStrategy();
    }

    @Bean
    public ErrorDispatcher errorDispatcher(List<ExceptionHandleStrategy<?>> strategies, DefaultExceptionStrategy fallback) {
        return new ErrorDispatcher(strategies, fallback);
    }

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    public CurrentUser currentUser() {
        return UserContextHolder.getContext();
    }

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    public CurrentTrace currentTrace() {
        return TraceContextHolder.getContext();
    }
}
