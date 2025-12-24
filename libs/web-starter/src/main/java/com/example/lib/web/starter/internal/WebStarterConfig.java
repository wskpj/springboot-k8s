package com.example.lib.web.starter.internal;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.web.core.cookie.CookieManager;
import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.filter.ResponseFilter;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.starter.internal.config.WebProperties;
import com.example.lib.web.starter.internal.cookie.StandardCookieManager;
import com.example.lib.web.starter.internal.dispatcher.DefaultApiResultDispatcher;
import com.example.lib.web.starter.internal.handler.StandardExceptionHandler;
import com.example.lib.web.starter.internal.handler.StandardResponseHandler;
import com.example.lib.web.starter.internal.strategy.DefaultExceptionStrategy;
import com.example.lib.web.starter.internal.strategy.DomainExceptionStrategy;
import com.example.lib.web.starter.internal.strategy.SystemExceptionStrategy;
import com.example.lib.web.starter.internal.strategy.ValidationExceptionStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 웹 스타터의 기본 설정을 제공하는 자동 설정 클래스입니다.
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebStarterConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean(CookieManager.class)
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    public CookieManager cookieManager(HttpServletRequest request, HttpServletResponse response) {
        return new StandardCookieManager(request, response);
    }

    @Bean
    @ConditionalOnMissingBean(ResponseFilter.class)
    public ResponseFilter responseFilter(WebProperties webProperties) {
        return ResponseFilter.ofPrefixes(webProperties.responseFilterPrefixes().toArray(String[]::new));
    }

    @Bean
    @ConditionalOnMissingBean(ApiResultDispatcher.class)
    public ApiResultDispatcher apiResultDispatcher(
            TraceContext traceContext, 
            List<ExceptionHandleStrategy<?>> strategies, 
            @org.springframework.beans.factory.annotation.Qualifier("defaultExceptionStrategy") ExceptionHandleStrategy<?> fallback) {
        return new DefaultApiResultDispatcher(traceContext, strategies, (DefaultExceptionStrategy) fallback);
    }


    // --- Exception Handle Strategy Beans ---

    @Bean
    @ConditionalOnMissingBean(name = "domainExceptionStrategy")
    public ExceptionHandleStrategy<?> domainExceptionStrategy() {
        return new DomainExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemExceptionStrategy")
    public ExceptionHandleStrategy<?> systemExceptionStrategy() {
        return new SystemExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "validationExceptionStrategy")
    public ExceptionHandleStrategy<?> validationExceptionStrategy() {
        return new ValidationExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultExceptionStrategy")
    public ExceptionHandleStrategy<?> defaultExceptionStrategy() {
        return new DefaultExceptionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(name = "standardExceptionHandler")
    public Object standardExceptionHandler(ApiResultDispatcher dispatcher) {
        return new StandardExceptionHandler(dispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(name = "standardResponseHandler")
    public ResponseBodyAdvice<Object> standardResponseHandler(
            ApiResultDispatcher dispatcher, 
            ObjectMapper objectMapper,
            ResponseFilter responseFilter) {
        return new StandardResponseHandler(dispatcher, objectMapper, responseFilter);
    }
}
