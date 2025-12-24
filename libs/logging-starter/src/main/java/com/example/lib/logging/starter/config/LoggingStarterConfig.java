package com.example.lib.logging.starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.example.lib.logging.starter.filter.MdcLoggingFilter;
import com.example.lib.logging.starter.filter.UserContextMdcFilter;

import lombok.RequiredArgsConstructor;

/**
 * 로깅 스타터의 기본 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
@RequiredArgsConstructor
public class LoggingStarterConfig {

    @Bean
    public MdcLoggingFilter mdcLoggingFilter() {
        return new MdcLoggingFilter();
    }

    @Bean
    public UserContextMdcFilter userContextMdcFilter() {
        return new UserContextMdcFilter();
    }

    @Bean
    public FilterRegistrationBean<MdcLoggingFilter> mdcLoggingFilterRegistration(MdcLoggingFilter filter) {
        FilterRegistrationBean<MdcLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        // 가장 먼저 실행 (TraceId, IP 추출)
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserContextMdcFilter> userContextMdcFilterRegistration(UserContextMdcFilter filter) {
        FilterRegistrationBean<UserContextMdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        // Spring Security Filter Chain (-100) 보다 나중에 실행되도록 설정
        // 그래야 UserContextHolder에 값이 채워진 상태로 로그를 찍음
        registrationBean.setOrder(0); 
        return registrationBean;
    }
}
