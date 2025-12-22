package com.example.lib.web.starter.filter.handler;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.starter.bean.ApiGenerator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 필터 계층에서 발생하는 예외를 잡아 공통 에러 응답으로 변환하는 필터입니다.
 */
@RequiredArgsConstructor
public class FilterExceptionHandlingFilter extends OncePerRequestFilter {

    private final ErrorDispatcher errorDispatcher;
    private final ApiGenerator apiGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 필터 예외를 Dispatcher로 전달하여 ApiError 생성 후 스트림으로 출력
            apiGenerator.writeStream(
                errorDispatcher.dispatch(e, request.getRequestURI()), 
                response
            );
        }
    }
}
