package com.example.springboot_app.global.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.exception.types.InfrastructureException;
import com.example.springboot_app.global.exception.types.SystemException;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(2)
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    /**
     * 비즈니스 로직 예외 처리 (500)
     */
    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<ApiError> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[Exception] Business Exception [{}]: {}", e.getErrorType().getCode(), e.getMessage());
        return responseError(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }
    
    /**
     * 시스템 예외 처리 (500)
     */
    @ExceptionHandler(SystemException.class)
    private ResponseEntity<ApiError> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("[Exception] System Exception [{}]: {}", e.getErrorType().getCode(), e.getMessage());
        return responseError(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 인프라 예외 처리 (500)
     */
    @ExceptionHandler(InfrastructureException.class)
    private ResponseEntity<ApiError> handleInfrastructureException(InfrastructureException e, HttpServletRequest request) {
        log.error("[Exception] Infrastructure Exception [{}]: {}", e.getErrorType().getCode(), e.getMessage());
        return responseError(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 인증 예외 처리 (401)
     */
    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("[Exception] Authentication Exception: {}", e.getMessage());
        return responseError(GlobalError.UNAUTHORIZED, request.getRequestURI(), e.getMessage());
    }
    
    /**
     * 보안 권한 예외 처리 (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("[Exception] Access Denied Exception: {}", e.getMessage());
        return responseError(GlobalError.FORBIDDEN, request.getRequestURI(), e.getMessage());
    }

    /**
     * 예상치 못한 예외 Fallback (500)
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiError> handleException(Exception e, HttpServletRequest request) {
        log.error("[Exception] Unhandled Exception: ", e);
        return responseError(GlobalError.INTERNAL_SERVER_ERROR, request.getRequestURI(), e.getMessage());
    }
}
