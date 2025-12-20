package com.example.springboot_app.global.filter.handler;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboot_app.global.bean.ApiGenerator;
import com.example.springboot_app.global.exception.dispatcher.ErrorResponseDispatcher;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 필터 체인에서 발생하는 예외를 통합 처리하는 필터
 */
@RequiredArgsConstructor
public class FilterExceptionHandlingFilter extends OncePerRequestFilter {

    private final ErrorResponseDispatcher dispatcher;
    private final ApiGenerator apiGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 필터 체인 내부의 예외를 디스패처를 통해 전략적으로 처리
            ApiError error = dispatcher.dispatch(e, request);
            apiGenerator.writeStream(error, response);
        }
    }
}
