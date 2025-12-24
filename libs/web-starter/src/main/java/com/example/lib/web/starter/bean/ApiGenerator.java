package com.example.lib.web.starter.bean;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;

import com.example.lib.common.core.context.trace.TraceContextHolder;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.response.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiGenerator {

    private final ObjectMapper objectMapper;

    /**
     * 바디 객체를 ApiResult로 감싸서 반환합니다.
     */
    public Object generate(Object body, ServerHttpResponse response) {
        String traceId = TraceContextHolder.getTraceId();
        
        if (body instanceof ApiResult) return body;

        if (body instanceof ApiError apiError) {
            if (response != null) response.setStatusCode(HttpStatusCode.valueOf(apiError.status()));

            return ApiResult.fail(apiError, traceId);
        }

        return ApiResult.ok(body, traceId);
    }

    /**
     * HttpServletResponse 스트림에 직접 에러를 기록합니다.
     */
    public void writeStream(ApiError error, HttpServletResponse response) throws IOException {
        String traceId = TraceContextHolder.getTraceId();
        
        response.setStatus(error.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        objectMapper.writeValue(response.getOutputStream(), ApiResult.fail(error, traceId));
    }
}
