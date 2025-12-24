package com.example.lib.logging.starter.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.logging.starter.internal.decorator.MdcTaskDecorator;
import com.example.lib.logging.starter.internal.filter.MdcLoggingFilter;
import com.example.lib.logging.starter.internal.filter.UserContextMdcFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 로깅 스타터의 기본 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
@RequiredArgsConstructor
public class LoggingStarterConfig {

    @Bean
    @ConditionalOnMissingBean(TaskDecorator.class)
    public TaskDecorator mdcTaskDecorator() {
        return new MdcTaskDecorator();
    }


    @Bean
    @ConditionalOnMissingBean(name = "mdcLoggingFilter")
    public OncePerRequestFilter mdcLoggingFilter() {
        return new MdcLoggingFilter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "userContextMdcFilter")
    public OncePerRequestFilter userContextMdcFilter() {
        return new UserContextMdcFilter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "mdcLoggingFilterRegistration")
    public FilterRegistrationBean<OncePerRequestFilter> mdcLoggingFilterRegistration(
            @Qualifier("mdcLoggingFilter") OncePerRequestFilter mdcLoggingFilter) {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(mdcLoggingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userContextMdcFilterRegistration")
    public FilterRegistrationBean<OncePerRequestFilter> userContextMdcFilterRegistration(
            @Qualifier("userContextMdcFilter") OncePerRequestFilter userContextMdcFilter) {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(userContextMdcFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0); 
        return registrationBean;
    }

    /**
     * 현재 추적 문맥을 제공하는 요청 스코프 프록시 빈입니다.
     * 필터에서 설정되지 않은 경우 예외를 발생시켜 소비자가 항상 존재를 보장받게 합니다.
     */
    @Bean
    @ConditionalOnMissingBean(name = "traceContext")
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TraceContext traceContext() {
        TraceContext context = TraceContextHolder.getContext();
        if (context == null) {
            throw new IllegalStateException("TraceContext is not initialized. Ensure MdcLoggingFilter is registered.");
        }
        return context;
    }
}
