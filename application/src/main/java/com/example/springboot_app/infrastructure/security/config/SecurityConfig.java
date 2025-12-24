package com.example.springboot_app.infrastructure.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.lib.security.starter.annotation.AuthAdmin;
import com.example.lib.security.starter.annotation.AuthPublic;
import com.example.lib.security.starter.bean.JwtProvider;
import com.example.lib.security.starter.filter.JwtAuthenticationFilter;
import com.example.lib.security.starter.handler.CustomAccessDeniedHandler;
import com.example.lib.security.starter.handler.CustomAuthenticationEntryPoint;
import com.example.lib.security.starter.resolver.AuthAnnotationResolver;
import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.bean.ApiGenerator;
import com.example.lib.logging.starter.filter.MdcLoggingFilter;
import com.example.lib.logging.starter.filter.UserContextMdcFilter;
import com.example.lib.web.starter.filter.handler.FilterExceptionHandlingFilter;
import com.example.springboot_app.infrastructure.redis.repository.GlobalRedisRepository;
import com.example.springboot_app.infrastructure.redis.service.RedisStringService;
import com.example.springboot_app.infrastructure.security.filter.GlobalRateLimitFilter;
import com.example.springboot_app.infrastructure.security.policy.RateLimitPolicy;
import com.example.lib.security.starter.config.SecurityStarterConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 설정 클래스입니다.
 * SecurityStarterConfig를 상속받아 공통 보안 빈들을 활성화합니다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityStarterConfig {

    public SecurityConfig(ObjectProvider<HandlerMappingIntrospector> introspectorProvider) {
        super(introspectorProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtProvider jwtProvider,
            AuthAnnotationResolver authResolver,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler,
            GlobalRedisRepository globalRedisRepository,
            RedisStringService redisStringService,
            ApiGenerator apiGenerator,
            List<RateLimitPolicy> rateLimitPolicies,
            ErrorDispatcher errorDispatcher,
            MdcLoggingFilter mdcLoggingFilter,
            UserContextMdcFilter userContextMdcFilter
    ) throws Exception {
        
        log.info("[SECURITY] Initializing Security Filter Chain...");

        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() 
                    .requestMatchers("/h2-console/**", "/actuator/**").permitAll()
                    .requestMatchers(request -> authResolver.hasAnnotation(request, AuthAdmin.class)).hasRole("ADMIN")
                    .requestMatchers(request -> authResolver.hasAnnotation(request, AuthPublic.class)).permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterBefore(new FilterExceptionHandlingFilter(errorDispatcher, apiGenerator), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(mdcLoggingFilter, FilterExceptionHandlingFilter.class)
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(userContextMdcFilter, JwtAuthenticationFilter.class)
            .addFilterAfter(new GlobalRateLimitFilter(globalRedisRepository, redisStringService, rateLimitPolicies), UserContextMdcFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
