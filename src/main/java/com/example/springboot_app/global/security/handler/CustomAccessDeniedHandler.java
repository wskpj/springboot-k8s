package com.example.springboot_app.global.security.handler;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.springboot_app.api.common.dto.ApiResult;
import com.example.springboot_app.api.common.dto.ErrorResponse;
import com.example.springboot_app.global.enums.GlobalError;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        String requestId = MDC.get("requestId");
        ErrorResponse errorResponse = ErrorResponse.of(GlobalError.FORBIDDEN, request.getRequestURI(), e.getMessage());
        ApiResult<Void> apiResult = ApiResult.fail(errorResponse, requestId);

        response.getWriter().write(objectMapper.writeValueAsString(apiResult));
    }
}
