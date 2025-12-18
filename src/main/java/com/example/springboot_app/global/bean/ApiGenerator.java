package com.example.springboot_app.global.bean;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.response.types.ApiError;
import com.example.springboot_app.global.response.types.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiGenerator {

    private final ObjectMapper objectMapper;

    /**
     * 응답을 ApiResult로 규격화(래핑)하는 메서드
     */
    public Object generate(Object body, ServerHttpResponse response) {
        String requestId = MDC.get("requestId");

        // [0/2] 이미 ApiResult인 경우 그대로 반환
        if (body instanceof ApiResult) return body;

        // [1/2] 에러 응답인 경우 fail로 래핑
        if (body instanceof ApiError apiError) {
            response.setStatusCode(HttpStatusCode.valueOf(apiError.getStatus()));
            return ApiResult.fail(apiError, requestId);
        }

        // [2/2] 성공 응답인 경우 success로 래핑
        return ApiResult.success(body, requestId);
    }

    /**
     * 응답 스트림을 직접 작성하는 메서드
     */
    public void writeStream(ApiError error, HttpServletResponse response) throws IOException {
        String requestId = MDC.get("requestId");

        // 응답 메타데이터 설정
        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 응답 스트림 작성
        objectMapper.writeValue(response.getOutputStream(), ApiResult.fail(error, requestId));
    }
}
