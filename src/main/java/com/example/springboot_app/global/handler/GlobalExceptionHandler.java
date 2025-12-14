package com.example.springboot_app.global.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.global.enums.ErrorType;
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
     * 예상치 못한 예외 Fallback (500)
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception: ", e);
        return responseError(ErrorType.INTERNAL_SERVER_ERROR, request.getRequestURI(), e.getMessage());
    }
}
