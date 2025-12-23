package com.example.springboot_app.infrastructure.security.config;

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

import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import com.example.lib.security.starter.config.SecurityStarterConfig;

/**
 * Spring Security 설정 클래스입니다.
 * SecurityStarterConfig를 상속받아 공통 보안 빈들을 활성화합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityStarterConfig {

    private final JwtProvider jwtProvider;
    private final AuthAnnotationResolver authResolver;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final GlobalRedisRepository globalRedisRepository;
    private final RedisStringService redisStringService;
    private final ApiGenerator apiGenerator;
    private final List<RateLimitPolicy> rateLimitPolicies;
    private final ErrorDispatcher errorDispatcher;
    
    // 로깅 및 예외 처리 필터 주입
    private final MdcLoggingFilter mdcLoggingFilter;
    private final UserContextMdcFilter userContextMdcFilter;

    public SecurityConfig(
            HandlerMappingIntrospector introspector,
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
            UserContextMdcFilter userContextMdcFilter) {
        super(introspector);
        this.jwtProvider = jwtProvider;
        this.authResolver = authResolver;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.globalRedisRepository = globalRedisRepository;
        this.redisStringService = redisStringService;
        this.apiGenerator = apiGenerator;
        this.rateLimitPolicies = rateLimitPolicies;
        this.errorDispatcher = errorDispatcher;
        this.mdcLoggingFilter = mdcLoggingFilter;
        this.userContextMdcFilter = userContextMdcFilter;
    }

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
            // 0. 필터 계층 통합 예외 처리 필터
            .addFilterBefore(new FilterExceptionHandlingFilter(errorDispatcher, apiGenerator), UsernamePasswordAuthenticationFilter.class)
            // 1. MDC 로깅 필터
            .addFilterBefore(mdcLoggingFilter, FilterExceptionHandlingFilter.class)
            // 2. JWT 필터
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            // 3. User Context MDC 필터
            .addFilterAfter(userContextMdcFilter, JwtAuthenticationFilter.class)
            // 4. Rate Limit 필터
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
