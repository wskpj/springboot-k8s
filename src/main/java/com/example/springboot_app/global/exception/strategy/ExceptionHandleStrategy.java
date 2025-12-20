package com.example.springboot_app.global.exception.strategy;

import com.example.springboot_app.global.response.types.ApiError;

/**
 * 예외 처리 전략 인터페이스
 */
public interface ExceptionHandleStrategy<E extends Exception> {
    
    /**
     * 해당 전략이 이 예외를 처리할 수 있는지 여부
     */
    boolean supports(Exception e);

    /**
     * 예외를 처리하여 규격화된 ApiError 반환
     */
    ApiError handle(E e, String path);

    /**
     * 에러 메시지 로깅
     */
    void log(E e, String path);
}
