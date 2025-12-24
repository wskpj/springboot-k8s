package com.example.lib.security.starter.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.security.starter.bean.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰을 검증하고 인증 정보를 설정하는 필터입니다.
 * 인증 성공 시 시큐리티 컨텍스트와 도메인 컨텍스트(UserContext)를 모두 설정합니다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = jwtProvider.resolveToken(request);

            if (token != null && jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserIdFromToken(token);
                String email = jwtProvider.getEmailFromToken(token);
                String role = jwtProvider.getRoleFromToken(token);

                String finalRole = role != null ? role : "ROLE_USER";
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(finalRole);

                // 1. Spring Security 컨텍스트 설정 (프레임워크 내부 호환성 및 표준 인터페이스 유지용)
                // 비즈니스 로직에서는 UserContext를 우선 사용하지만, 시큐리티 필터 체인 및 관련 라이브러리 지원을 위해 유지합니다.
                User userDetails = new User(String.valueOf(userId), "", List.of(authority));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 2. 도메인 UserContext 설정 (비즈니스 로직용)
                UserContext userContext = new UserContext(
                    userId,
                    email,
                    Set.of(finalRole),
                    Collections.emptyMap()
                );
                UserContextHolder.setContext(userContext);
            }

            filterChain.doFilter(request, response);
        } finally {
            // 요청이 끝나면 스레드 로컬 메모리 누수 방지를 위해 컨텍스트를 비움
            UserContextHolder.clearContext();
        }
    }
}
