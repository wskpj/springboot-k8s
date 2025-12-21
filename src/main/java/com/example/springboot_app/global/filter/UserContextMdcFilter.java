package com.example.springboot_app.global.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 보안 필터 이후에 동작하며 인증된 사용자의 식별자를 MDC에 추가합니다.
 */
public class UserContextMdcFilter extends OncePerRequestFilter {

    public static final String MDC_USER_INFO_KEY = "userInfo";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            MDC.put(MDC_USER_INFO_KEY, "user " + authentication.getName());
        } else {
            MDC.put(MDC_USER_INFO_KEY, "guest");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_INFO_KEY);
        }
    }
}
