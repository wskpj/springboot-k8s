package com.example.springboot_app.global.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboot_app.global.context.UserContext;
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

        UserContext context = userContextResolver.getCurrentUserContext()
                .orElseGet(UserContext::guest);

        MDC.put(MDC_USER_INFO_KEY, context.userId());
        // 추가 정보를 MDC에 더 넣고 싶다면 여기서 확장 가능
        // MDC.put("userRoles", context.roles().toString());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_INFO_KEY);
        }
    }
}
