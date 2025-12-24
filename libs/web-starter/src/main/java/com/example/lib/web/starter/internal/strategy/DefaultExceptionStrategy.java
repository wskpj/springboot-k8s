package com.example.lib.web.starter.internal.strategy;

import com.example.lib.web.core.error.GlobalError;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 처리되지 않은 모든 예외를 최종적으로 처리하는 기본 전략입니다.
 */
@Slf4j
public class DefaultExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return true; // 최후의 보루이므로 모든 예외 수용
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        return ApiError.of(GlobalError.INTERNAL_SERVER_ERROR, path, "서버 내부 오류가 발생했습니다.");
    }

    @Override
    public void log(Exception e, String path) {
        log.error("[WebStarter] Unhandled Exception at {}: {}", path, e.getMessage(), e);
    }
}