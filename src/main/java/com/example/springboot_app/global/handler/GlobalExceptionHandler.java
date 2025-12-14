package com.example.springboot_app.global.handler;

import com.example.springboot_app.global.error.exception.BusinessException;
import com.example.springboot_app.global.error.exception.InfrastructureException;
import com.example.springboot_app.global.error.exception.SystemException;
import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.api.common.dto.ErrorResponse;
import com.example.springboot_app.global.enums.ErrorType;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, 
            HttpServletRequest request) {
        log.info("Validation Error: {}", e.getMessage());

        // 에러 목록에서 필요한 정보 추출
        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return responseError(ErrorType.INVALID_INPUT_VALUE, request, details);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("Business Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(InfrastructureException.class)
    protected ResponseEntity<ApiResult<Void>> handleInfrastructureException(InfrastructureException e, HttpServletRequest request) {
        log.error("Infrastructure Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ApiResult<Void>> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("Base Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<ApiResult<Void>> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("System Exception [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        return responseError(e.getErrorCode(), request, e.getDetails());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception: ", e);
        return responseError(ErrorType.INTERNAL_SERVER_ERROR, request, e.getMessage());
    }

    private ResponseEntity<ApiResult<Void>> responseError(ErrorType errorCode, HttpServletRequest request, Object details) {
        String requestId = MDC.get("requestId");

        ErrorResponse httpError = ErrorResponse.of(errorCode, request.getRequestURI(), details);
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResult.fail(httpError, requestId));
    }

    private record FieldErrorDetail(String field, String message) {}
}
