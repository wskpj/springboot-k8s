package com.example.springboot_app.global.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.global.enums.GlobalError;
import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.error.exception.InfrastructureException;
import com.example.springboot_app.global.error.exception.SystemException;

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
    private ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request.getRequestURI(), e.getDetails());
    }
    
    /**
     * 시스템 예외 처리 (500)
     */
    @ExceptionHandler(SystemException.class)
    private ResponseEntity<ApiResult<Void>> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("System Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 인프라 예외 처리 (500)
     */
    @ExceptionHandler(InfrastructureException.class)
    private ResponseEntity<ApiResult<Void>> handleInfrastructureException(InfrastructureException e, HttpServletRequest request) {
        log.error("Infrastructure Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 인증 예외 처리 (401)
     */
    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ApiResult<Void>> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("Authentication Exception: {}", e.getMessage());
        return responseError(GlobalError.UNAUTHORIZED, request.getRequestURI(), e.getMessage());
    }
    
    /**
     * 보안 권한 예외 처리 (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ApiResult<Void>> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("Access Denied Exception: {}", e.getMessage());
        return responseError(GlobalError.FORBIDDEN, request.getRequestURI(), e.getMessage());
    }

    /**
     * 예상치 못한 예외 Fallback (500)
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception: ", e);
        return responseError(GlobalError.INTERNAL_SERVER_ERROR, request.getRequestURI(), e.getMessage());
    }
}
