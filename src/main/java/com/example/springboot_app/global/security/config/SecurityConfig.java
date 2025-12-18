package com.example.springboot_app.global.security.config;   

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.springboot_app.domain.auth.annotations.AuthAdmin;
import com.example.springboot_app.domain.auth.annotations.AuthPublic;
import com.example.springboot_app.global.bean.ApiGenerator;
import com.example.springboot_app.global.redis.repository.GlobalRedisRepository;
import com.example.springboot_app.global.redis.service.RedisStringService;
import com.example.springboot_app.global.security.GlobalRateLimitFilter;
import com.example.springboot_app.global.security.JwtAuthenticationFilter;
import com.example.springboot_app.global.security.JwtProvider;
import com.example.springboot_app.global.security.handler.CustomAccessDeniedHandler;
import com.example.springboot_app.global.security.handler.CustomAuthenticationEntryPoint;
import com.example.springboot_app.global.security.policy.RateLimitPolicy;
import com.example.springboot_app.global.security.resolver.AuthAnnotationResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final AuthAnnotationResolver authResolver;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final GlobalRedisRepository globalRedisRepository;
    private final RedisStringService redisStringService;
    private final ApiGenerator apiGenerator;
    private final List<RateLimitPolicy> rateLimitPolicies;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // for H2 console
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() 
                    .requestMatchers("/h2-console/**", "/actuator/**").permitAll()
                    // 어노테이션 기반 커스텀 권한 검증 로직
                    // @AuthAdmin - ROLE_ADMIN 체크
                    // @AuthPublic - 인증 없이 통과
                    // 나머지는 Spring Security 기본 인증 필요
                    .requestMatchers(request -> authResolver.hasAnnotation(request, AuthAdmin.class)).hasRole("ADMIN")
                    .requestMatchers(request -> authResolver.hasAnnotation(request, AuthPublic.class)).permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new GlobalRateLimitFilter(globalRedisRepository, redisStringService, apiGenerator, rateLimitPolicies), JwtAuthenticationFilter.class);

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
