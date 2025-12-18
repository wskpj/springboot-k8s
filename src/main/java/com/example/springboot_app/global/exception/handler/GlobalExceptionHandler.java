package com.example.springboot_app.global.exception.handler;

import org.springframework.core.annotation.Order;
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
    private ApiError handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[Exception] Business Exception {}", e.getMessage());
        return errorInstance(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }
    
    /**
     * 시스템 예외 처리 (500)
     */
    @ExceptionHandler(SystemException.class)
    private ApiError handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("[Exception] System Exception {}", e.getMessage());
        return errorInstance(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 인프라 예외 처리 (500)
     */
    @ExceptionHandler(InfrastructureException.class)
    private ApiError handleInfrastructureException(InfrastructureException e, HttpServletRequest request) {
        log.error("[Exception] Infrastructure Exception {}", e.getMessage());
        return errorInstance(e.getErrorType(), request.getRequestURI(), e.getDetails());
    }

    /**
     * 예상치 못한 예외 Fallback (500)
     */
    @ExceptionHandler(Exception.class)
    private ApiError handleException(Exception e, HttpServletRequest request) {
        log.error("[Exception] Unhandled Exception: {}", e.getMessage());
        return errorInstance(GlobalError.INTERNAL_SERVER_ERROR, request.getRequestURI(), e.getMessage());
    }
}
