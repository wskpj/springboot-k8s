package com.example.lib.web.starter.internal.dispatcher;

import java.util.List;

import com.example.lib.common.core.context.trace.TraceContext;
import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.response.ApiResult;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.starter.internal.strategy.DefaultExceptionStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ApiResultDispatcher의 기본 구현체입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultApiResultDispatcher implements ApiResultDispatcher {

    private final TraceContext traceContext;
    private final List<ExceptionHandleStrategy<?>> strategies;
    private final DefaultExceptionStrategy fallback;

    @Override
    public <T> ApiResult<T> dispatch(T data) {
        String traceId = traceContext.trace().traceId();
        return ApiResult.ok(data, traceId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ApiResult<?> dispatch(Exception e, String uri) {
        String traceId = traceContext.trace().traceId();

        ExceptionHandleStrategy<Exception> strategy = (ExceptionHandleStrategy<Exception>) strategies.stream()
                .filter(s -> !(s instanceof DefaultExceptionStrategy))
                .filter(s -> s.supports(e))
                .findFirst()
                .orElse((ExceptionHandleStrategy) fallback);

        ApiError error = strategy.handle(e, uri);
        return ApiResult.fail(error, traceId);
    }
}
