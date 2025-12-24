package com.example.lib.web.starter.internal.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.response.ApiResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * REST 컨트롤러의 예외를 통합 처리하는 표준 핸들러입니다.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class StandardExceptionHandler {

    private final ApiResultDispatcher dispatcher;

    /**
     * 모든 예외를 포괄적으로 처리하여 규격화된 ApiResult(Error) 응답을 반환합니다.
     */
    @ExceptionHandler(Exception.class)
    public ApiResult<?> handleAllException(Exception e, HttpServletRequest request) {
        return dispatcher.dispatch(e, request.getRequestURI());
    }
}
