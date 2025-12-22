package com.example.lib.security.starter.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.bean.ApiGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 인가(Authorization) 실패 시 처리를 담당하는 핸들러입니다.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ErrorDispatcher errorDispatcher;
    private final ApiGenerator apiGenerator;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        apiGenerator.writeStream(
            errorDispatcher.dispatch(accessDeniedException, request.getRequestURI()), 
            response
        );
    }
}
