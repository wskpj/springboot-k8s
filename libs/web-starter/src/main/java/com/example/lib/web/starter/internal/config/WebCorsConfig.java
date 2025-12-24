package com.example.lib.web.starter.internal.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

/**
 * 글로벌 CORS 필터 설정입니다.
 * 이 빈은 시큐리티 유무와 상관없이 서블릿 필터로 동작하며,
 * 시큐리티가 활성화된 경우 시큐리티 필터 체인 내부로 자동 편입될 수 있습니다.
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnProperty(prefix = "web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebCorsConfig {

    private final WebProperties webProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(webProperties.allowedOrigins());
        config.setAllowedMethods(webProperties.allowedMethods());
        config.setAllowedHeaders(webProperties.allowedHeaders());
        config.setAllowCredentials(webProperties.allowCredentials());
        config.setMaxAge(webProperties.maxAge());

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
