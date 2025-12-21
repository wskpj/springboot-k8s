package com.example.springboot_app.global.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String MDC_REQUEST_ID_KEY = "requestId";
    public static final String MDC_CLIENT_IP_KEY = "clientIp";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Request ID
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        // 2. Extract Client IP (Considering Proxy/L7)
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isBlank() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        } else {
            // X-Forwarded-For can contain multiple IPs, the first one is the original client
            clientIp = clientIp.split(",")[0].trim();
        }

        // 3. Put to MDC
        MDC.put(MDC_REQUEST_ID_KEY, requestId);
        MDC.put(MDC_CLIENT_IP_KEY, clientIp);
        
        request.setAttribute(MDC_REQUEST_ID_KEY, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 4. Log Request Completed          
            long elapsed = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String method = request.getMethod();
            String uri = request.getRequestURI();

            if (status >= 400) {
                log.warn("[Request] {} {} - {} ({}ms)", method, uri, status, elapsed);
            } else {
                log.debug("[Request] {} {} - {} ({}ms)", method, uri, status, elapsed);
            }

            // 5. Clear MDC
            MDC.remove(MDC_REQUEST_ID_KEY);
            MDC.remove(MDC_CLIENT_IP_KEY);
        }
    }
}
