package com.example.springboot_app.infrastructure.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.security.core.annotation.AuthAdmin;
import com.example.lib.security.core.annotation.AuthPublic;
import com.example.lib.security.core.resolver.AuthResolver;
import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.starter.internal.filter.StandardFilterExceptionFilter;

import com.example.springboot_app.infrastructure.redis.repository.GlobalRedisRepository;
import com.example.springboot_app.infrastructure.redis.service.RedisStringService;
import com.example.springboot_app.infrastructure.security.filter.GlobalRateLimitFilter;
import com.example.springboot_app.infrastructure.security.policy.RateLimitPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 설정 클래스입니다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthResolver authResolver,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler,
            GlobalRedisRepository globalRedisRepository,
            RedisStringService redisStringService,
            ApiResultDispatcher dispatcher,
            ObjectMapper objectMapper,
            List<RateLimitPolicy> rateLimitPolicies,
            UserContext userContext, // UserContext 프록시 빈 주입
            @Qualifier("mdcLoggingFilter") OncePerRequestFilter mdcLoggingFilter,
            @Qualifier("userContextMdcFilter") OncePerRequestFilter userContextMdcFilter,
            @Qualifier("jwtAuthenticationFilter") OncePerRequestFilter jwtAuthenticationFilter
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
            .addFilterBefore(new StandardFilterExceptionFilter(dispatcher, objectMapper), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(mdcLoggingFilter, StandardFilterExceptionFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(userContextMdcFilter, jwtAuthenticationFilter.getClass())
            .addFilterAfter(new GlobalRateLimitFilter(globalRedisRepository, redisStringService, rateLimitPolicies, userContext), userContextMdcFilter.getClass());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
