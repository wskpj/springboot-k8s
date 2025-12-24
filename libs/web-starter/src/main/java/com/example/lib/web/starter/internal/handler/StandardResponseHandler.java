package com.example.lib.web.starter.internal.handler;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.lib.web.core.dispatcher.ApiResultDispatcher;
import com.example.lib.web.core.filter.ResponseFilter;
import com.example.lib.web.core.response.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 모든 REST 컨트롤러의 응답을 가로채어 ApiResult 규격으로 자동 래핑하는 핸들러입니다.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class StandardResponseHandler implements ResponseBodyAdvice<Object> {

    private final ApiResultDispatcher dispatcher;
    private final ObjectMapper objectMapper;
    private final ResponseFilter responseFilter;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();

        if (Resource.class.isAssignableFrom(parameterType) || byte[].class.isAssignableFrom(parameterType)) {
            return false;
        }

        return responseFilter.supports(returnType);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType, 
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ApiResult) {
            return body;
        }

        Object wrappedBody = dispatcher.dispatch(body);

        // StringHttpMessageConverter가 선택된 경우 수동으로 JSON 문자열 변환
        if (StringHttpMessageConverter.class.isAssignableFrom(converterType)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(wrappedBody);
        }

        return wrappedBody;
    }
}
