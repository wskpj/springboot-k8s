package com.example.lib.logging.starter.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.example.lib.logging.starter.filter.MdcLoggingFilter;
import com.example.lib.logging.starter.filter.UserContextMdcFilter;

import lombok.RequiredArgsConstructor;

/**
 * 로깅 스타터의 기본 설정을 담은 추상 클래스입니다.
 */
@RequiredArgsConstructor
public abstract class LoggingStarterConfig {

    @Bean
    public MdcLoggingFilter mdcLoggingFilter() {
        return new MdcLoggingFilter();
    }

    @Bean
    public UserContextMdcFilter userContextMdcFilter() {
        return new UserContextMdcFilter();
    }

    @Bean
    public FilterRegistrationBean<UserContextMdcFilter> userContextMdcFilterRegistration(UserContextMdcFilter filter) {
        FilterRegistrationBean<UserContextMdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        // TraceIdFilter(Ordered.HIGHEST_PRECEDENCE) 보다 다음에 실행되도록 설정
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
}
