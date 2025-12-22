package com.example.lib.web.core.dispatcher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.lib.web.core.strategy.DefaultExceptionStrategy;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.response.ApiError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorDispatcher {

    private final List<ExceptionHandleStrategy<?>> strategies;
    private final DefaultExceptionStrategy fallback;

    @SuppressWarnings("unchecked")
    public ApiError dispatch(Exception e, String uri) {

        ExceptionHandleStrategy<Exception> strategy = (ExceptionHandleStrategy<Exception>) strategies.stream()
                .filter(s -> !(s instanceof DefaultExceptionStrategy))
                .filter(s -> s.supports(e))
                .findFirst()
                .orElse((ExceptionHandleStrategy) fallback);

        return strategy.handle(e, uri);
    }
}
