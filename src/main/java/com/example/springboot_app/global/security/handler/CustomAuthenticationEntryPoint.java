package com.example.springboot_app.global.security.handler;
import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.bean.ApiGenerator;
import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApiGenerator apiGenerator;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        ApiError error = ApiError.of(GlobalError.UNAUTHORIZED, request.getRequestURI(), e.getMessage());
        apiGenerator.writeStream(error, response);
    }
}
