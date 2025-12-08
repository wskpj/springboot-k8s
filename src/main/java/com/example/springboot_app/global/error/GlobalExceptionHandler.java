package com.example.springboot_app.global.error;

import com.example.springboot_app.common.dto.ApiResult;
import com.example.springboot_app.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Input Validation 예외 (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("handleMethodArgumentNotValidException: {}", e.getMessage());
        String requestId = (String) request.getAttribute("requestId");
        if (requestId == null) requestId = UUID.randomUUID().toString();

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );

        HttpError httpError = HttpError.of(ErrorCode.INVALID_INPUT_VALUE, requestId, request.getRequestURI(), errors);
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ApiResult.fail(httpError));
    }

    // 비즈니스 로직 예외 (예상된 예외)
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ApiResult<Void>> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("handleBaseException: {}", e.getMessage());
        String requestId = (String) request.getAttribute("requestId");
        if (requestId == null) requestId = UUID.randomUUID().toString();

        HttpError httpError = HttpError.of(e.getErrorCode(), requestId, request.getRequestURI(), e.getDetails());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResult.fail(httpError));
    }

    // 기타 시스템 예외 (예상치 못한 예외)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("handleEntityNotFoundException", e);
        String requestId = (String) request.getAttribute("requestId");
        if (requestId == null) requestId = UUID.randomUUID().toString();

        HttpError httpError = HttpError.of(ErrorCode.INTERNAL_SERVER_ERROR, requestId, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResult.fail(httpError));
    }
}
