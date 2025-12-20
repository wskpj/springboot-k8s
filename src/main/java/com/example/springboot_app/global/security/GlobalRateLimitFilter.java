package com.example.springboot_app.global.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboot_app.global.exception.types.RateLimitException;
import com.example.springboot_app.global.redis.dto.KeyBinding;
import com.example.springboot_app.global.redis.enums.RedisStringKey;
import com.example.springboot_app.global.redis.repository.GlobalRedisRepository;
import com.example.springboot_app.global.redis.service.RedisStringService;
import com.example.springboot_app.global.security.policy.RateLimitPolicy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalRateLimitFilter extends OncePerRequestFilter {

    private final GlobalRedisRepository globalRedisRepository;
    private final RedisStringService redisStringService;
    private final List<RateLimitPolicy> policies;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // 1. 전략 패턴을 사용하여 현재 요청에 맞는 정책 선택
        RateLimitPolicy policy = policies.stream()
                .filter(p -> p.supports(request))
                .findFirst()
                .orElseThrow(() -> new ServletException("No rate limit policy found"));

        int limit = policy.getLimit();
        long duration = policy.getDuration();
        String requestUri = request.getRequestURI();

        // 2. 클라이언트 식별자 추출 (로그인 유저는 ID, 아니면 IP)
        String identifier = getClientIdentifier(request);
        
        // 3. Redis 키 생성 및 카운트 증가 (Repository를 통한 루아 스크립트 실행)
        Long count = globalRedisRepository.checkAndIncrementRateLimit(identifier, requestUri, duration);
        
        // 5. 헤더에 Rate Limit 정보 추가
        long remaining = count != null ? Math.max(0, limit - count) : 0;
        
        // TTL은 여전히 RedisStringService를 통해 조회 가능
        KeyBinding<RedisStringKey> key = RedisStringKey.RATE_LIMIT.bind(identifier, requestUri);
        long reset = redisStringService.getExpire(key);
        
        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Reset", String.valueOf(reset > 0 ? reset : duration));

        // 6. 제한 수치 초과 시 에러 응답 및 요청 차단
        if (count != null && count > limit) {
            throw new RateLimitException("Rate limit exceeded. Try again later.");
        }

        // 7. 제한 통과 시 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private String getClientIdentifier(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthUser authUser) {
                return String.valueOf(authUser.getId());
            }
            return authentication.getName();
        }
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
