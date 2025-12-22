package com.example.springboot_app.global.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.springboot_app.global.context.UserContextResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 컨텍스트를 MDC에 추가합니다.
 */
@RequiredArgsConstructor
public class UserContextMdcFilter extends OncePerRequestFilter {

    public static final String MDC_USER_INFO_KEY = "userInfo";
    private final UserContextResolver userContextResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        userContextResolver.getCurrentUserIdentifier()
            .ifPresentOrElse(
                user -> MDC.put(MDC_USER_INFO_KEY, "user " + user),
                () -> MDC.put(MDC_USER_INFO_KEY, "guest")
            );

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_INFO_KEY);
        }
    }
}
