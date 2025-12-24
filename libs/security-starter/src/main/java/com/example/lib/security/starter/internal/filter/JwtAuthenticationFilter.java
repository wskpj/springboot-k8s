package com.example.lib.security.starter.internal.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.common.core.context.user.CurrentUser;
import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;
import com.example.lib.security.starter.internal.bean.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰을 검증하고 사용자 문맥을 설정하는 필터입니다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Long userId = jwtProvider.getUserIdFromToken(token);
            String email = jwtProvider.getEmailFromToken(token);
            String role = jwtProvider.getRoleFromToken(token);

            // 코어의 표준 Default 레코드를 사용하여 컨텍스트 생성
            CurrentUser userData = new CurrentUser.Default(
                userId,
                email,
                token,
                Collections.singleton(role),
                Collections.emptyMap(),
                false
            );
            
            UserContextHolder.setContext(new UserContext(userData));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clearContext();
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
