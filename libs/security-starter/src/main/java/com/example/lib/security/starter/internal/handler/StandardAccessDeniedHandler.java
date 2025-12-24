package com.example.lib.security.starter.internal.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.response.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인가 실패(403 Forbidden) 시 ApiResult 규격으로 응답하는 핸들러입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class StandardAccessDeniedHandler implements AccessDeniedHandler {

    private final ApiResultDispatcher dispatcher;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        log.warn("[Security] Access Denied: {}", accessDeniedException.getMessage());

        // 통합 디스패처를 통해 ApiResult 생성
        ApiResult<?> result = dispatcher.dispatch(accessDeniedException, request.getRequestURI());
        
        response.setStatus(result.error().status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
    }
}
