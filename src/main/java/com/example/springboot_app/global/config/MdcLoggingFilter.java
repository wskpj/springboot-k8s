package com.example.springboot_app.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get Request ID from header or generate a new one
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        // Put request ID to MDC
        MDC.put(MDC_REQUEST_ID_KEY, requestId);

        // Also add RequestId to the request attributes so other components (like GlobalExceptionHandler) can use it easily
        request.setAttribute(MDC_REQUEST_ID_KEY, requestId);

        // Add to response header
        response.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Must clear MDC at the end of the request
            MDC.clear();
        }
    }
}
