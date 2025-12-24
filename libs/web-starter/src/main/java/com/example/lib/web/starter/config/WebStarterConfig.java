package com.example.lib.web.starter.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.lib.common.core.context.trace.CurrentTrace;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.common.core.context.user.CurrentUser;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.web.core.cookie.CookieManager;
import com.example.lib.web.core.cookie.StandardCookieManager;
import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.core.strategy.DefaultExceptionStrategy;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.strategy.error.SystemExceptionStrategy;
import com.example.lib.web.core.strategy.info.ValidationExceptionStrategy;
import com.example.lib.web.core.strategy.warn.DomainExceptionStrategy;
import com.example.lib.web.starter.bean.ApiGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 웹 스타터의 기본 설정을 제공하는 자동 설정 클래스입니다.
 * WebMvcConfigurer를 구현하여 기본 웹 설정을 제공합니다.
 */
@AutoConfiguration
public class WebStarterConfig implements WebMvcConfigurer {

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    public CookieManager cookieManager(HttpServletRequest request, HttpServletResponse response) {
        return new StandardCookieManager(request, response);
    }

    @Bean
    public ApiGenerator apiGenerator(ObjectMapper objectMapper) {
        return new ApiGenerator(objectMapper);
    }

    // --- Exception Handle Strategy Beans ---

    @Bean
    public DomainExceptionStrategy domainExceptionStrategy() {
        return new DomainExceptionStrategy();
    }

    @Bean
    public SystemExceptionStrategy systemExceptionStrategy() {
        return new SystemExceptionStrategy();
    }

    @Bean
    public ValidationExceptionStrategy validationExceptionStrategy() {
        return new ValidationExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultExceptionStrategy defaultExceptionStrategy() {
        return new DefaultExceptionStrategy();
    }

    @Bean
    public ErrorDispatcher errorDispatcher(List<ExceptionHandleStrategy<?>> strategies, DefaultExceptionStrategy fallback) {
        return new ErrorDispatcher(strategies, fallback);
    }

    // --- Context Proxy Beans ---

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
