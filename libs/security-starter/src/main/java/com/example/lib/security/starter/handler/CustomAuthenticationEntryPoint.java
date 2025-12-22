package com.example.lib.security.starter.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.bean.ApiGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 인증(Authentication) 실패 시 처리를 담당하는 엔트리 포인트입니다.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ErrorDispatcher errorDispatcher;
    private final ApiGenerator apiGenerator;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        apiGenerator.writeStream(
            errorDispatcher.dispatch(authException, request.getRequestURI()), 
            response
        );
    }
}
