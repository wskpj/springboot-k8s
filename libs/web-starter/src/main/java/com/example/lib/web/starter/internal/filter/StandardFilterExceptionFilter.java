package com.example.lib.web.starter.internal.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.response.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 필터 체인 내에서 발생하는 예외를 잡아 ApiResult 규격으로 응답하는 필터입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class StandardFilterExceptionFilter extends OncePerRequestFilter {

    private final ApiResultDispatcher dispatcher;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[Filter] Exception caught in filter chain: {}", e.getMessage(), e);
            handleException(request, response, e);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        // 통합 디스패처를 통해 ApiResult 생성
        ApiResult<?> result = dispatcher.dispatch(e, request.getRequestURI());
        
        response.setStatus(result.error().status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
    }
}
