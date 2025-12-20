package com.example.springboot_app.global.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.global.exception.dispatcher.ErrorResponseDispatcher;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends BaseExceptionHandler {

    private final ErrorResponseDispatcher dispatcher;

    /**
     * 모든 예외를 통합 처리
     * 상세 처리 및 로깅은 ErrorResponseDispatcher 및 각 전략에서 수행함
     */
    @ExceptionHandler(Exception.class)
    public ApiError handleAllException(Exception e, HttpServletRequest request) {
        return dispatcher.dispatch(e, request);
    }
}
