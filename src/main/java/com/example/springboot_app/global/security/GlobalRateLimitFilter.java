package com.example.springboot_app.global.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboot_app.global.bean.ApiGenerator;
import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisStringKey;
import com.example.springboot_app.global.redis.service.RedisStringService;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalRateLimitFilter extends OncePerRequestFilter {

    private static final String AUTH_PATH_PREFIX = "/api/v1/auth";
    private static final int GLOBAL_LIMIT = 500;
    private static final int AUTH_LIMIT = 10;
    private static final long DURATION = 60L;

    private final RedisStringService redisStringService;
    private final ApiGenerator apiGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // 1. 요청 URI에 따른 차등 제한 수치 결정 (인증 경로는 더 엄격하게)
        String requestUri = request.getRequestURI();
        int limit = requestUri.startsWith(AUTH_PATH_PREFIX) ? AUTH_LIMIT : GLOBAL_LIMIT;

        // 2. 클라이언트 식별자 추출 (로그인 유저는 ID, 아니면 IP)
        String identifier = getClientIdentifier(request);
        
        // 3. Redis 키 생성 및 카운트 증가 (Atomic 연산)
        KeyBinding<RedisStringKey> key = RedisStringKey.RATE_LIMIT.bind(identifier, requestUri);
        Long count = redisStringService.increment(key);
        
        // 4. 최초 요청 시 만료 시간(1분) 설정
        if (count != null && count == 1) {
            redisStringService.expire(key, DURATION);
        }
        
        // 5. 헤더에 Rate Limit 정보 추가
        long remaining = count != null ? Math.max(0, limit - count) : 0;
        long reset = redisStringService.getExpire(key);
        
        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Reset", String.valueOf(reset > 0 ? reset : DURATION));

        // 6. 제한 수치 초과 시 에러 응답 및 요청 차단
        if (count != null && count > limit) {
            ApiError error = ApiError.of(GlobalError.TOO_MANY_REQUESTS, requestUri, "Rate limit exceeded. Try again later.");
            apiGenerator.writeStream(error, response);
            return;
        }

        // 7. 제한 통과 시 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 클라이언트를 식별하기 위한 고유 키를 추출합니다.
     * 로그인된 사용자는 User ID를, 비로그인 사용자는 IP 주소를 사용합니다.
     */
    private String getClientIdentifier(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 시큐리티 컨텍스트에 인증 정보가 있는 경우
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthUser authUser) {
                return String.valueOf(authUser.getId()); // 사용자 PK 사용
            }
            return authentication.getName();
        }
        
        // 비로그인 사용자인 경우 IP 주소 사용 (프록시 고려)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
