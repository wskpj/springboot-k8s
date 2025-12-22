package com.example.lib.web.starter.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.lib.web.core.dispatcher.ErrorDispatcher;
import com.example.lib.web.core.response.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * REST 컨트롤러의 예외를 통합 처리하는 추상 어드바이스 클래스입니다.
 * 이 라이브러리를 사용하는 애플리케이션은 이 클래스를 상속받아 
 * @RestControllerAdvice 를 선언하여 사용합니다.
 */
@RequiredArgsConstructor
public abstract class BaseExceptionHandler {

    private final ErrorDispatcher dispatcher;

    /**
     * 애플리케이션 예외를 통합 처리합니다.
     * 상세 처리 및 로깅은 ErrorDispatcher 및 각 전략(Strategy)에서 수행합니다.
     */
    @ExceptionHandler(Exception.class)
    public ApiError handleAllException(Exception e, HttpServletRequest request) {
        return dispatcher.dispatch(e, request.getRequestURI());
    }
}
