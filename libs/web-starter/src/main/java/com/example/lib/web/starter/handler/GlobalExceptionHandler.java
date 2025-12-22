package com.example.lib.web.starter.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.core.response.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * REST 컨트롤러의 예외를 통합 처리하는 어드바이스입니다.
 * 실제 처리 로직은 ErrorDispatcher를 통해 각 전략(Strategy)으로 위임됩니다.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorDispatcher dispatcher;

    /**
     * 애플리케이션 예외를 통합 처리합니다.
     * 상세 처리 및 로깅은 ErrorDispatcher 및 각 전략에서 수행합니다.
     */
    @ExceptionHandler(Exception.class)
    public ApiError handleAllException(Exception e, HttpServletRequest request) {
        return dispatcher.dispatch(e, request.getRequestURI());
    }
}
