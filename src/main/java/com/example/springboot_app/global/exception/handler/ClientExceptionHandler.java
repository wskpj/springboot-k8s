package com.example.springboot_app.global.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.global.response.dispatcher.ErrorResponseDispatcher;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@RestControllerAdvice
@RequiredArgsConstructor
public class ClientExceptionHandler extends BaseExceptionHandler {

    private final ErrorResponseDispatcher dispatcher;

    /**
     * @Valid 어노테이션으로 인한 유효성 검사 실패 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ApiError handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        log.info("[Exception] Validation Error: {}", e.getMessage());
        return dispatcher.dispatch(e, request.getRequestURI());
    }

    /**
     * 잘못된 인자 전달 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private ApiError handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.info("[Exception] Illegal Argument Exception: {}", e.getMessage());
        return dispatcher.dispatch(e, request.getRequestURI());
    }

    /**
     * 잘못된 프로퍼티 참조 시 발생하는 예외 처리 (400)
     */
    @ExceptionHandler(PropertyReferenceException.class)
    private ApiError handlePropertyReferenceException(PropertyReferenceException e, HttpServletRequest request) {
        log.info("[Exception] Property Reference Exception: {}", e.getMessage());
        return dispatcher.dispatch(e, request.getRequestURI());
    }
    
    /**
     * 인증 예외 처리 (401)
     */
    @ExceptionHandler(AuthenticationException.class)
    private ApiError handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("[Exception] Authentication Exception: {}", e.getMessage());
        return dispatcher.dispatch(e, request.getRequestURI());
    }
    
    /**
     * 보안 권한 예외 처리 (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    private ApiError handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("[Exception] Access Denied Exception: {}", e.getMessage());
        return dispatcher.dispatch(e, request.getRequestURI());
    }
}
