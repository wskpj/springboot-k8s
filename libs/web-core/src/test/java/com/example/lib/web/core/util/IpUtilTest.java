package com.example.lib.web.core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("IpUtil 테스트")
class IpUtilTest {

    @Nested
    @DisplayName("X-Forwarded-For 헤더 처리")
    class XForwardedFor {

        @Test
        @DisplayName("X-Forwarded-For에 단일 IP가 있으면 해당 IP를 반환한다")
        void returnsSingleIp() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");

            assertThat(IpUtil.getClientIp(request)).isEqualTo("192.168.1.1");
        }

        @Test
        @DisplayName("X-Forwarded-For에 IP 목록이 있으면 첫 번째 IP(클라이언트)를 반환한다")
        void returnsFirstIpFromList() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 172.16.0.1, 192.168.0.1");

            assertThat(IpUtil.getClientIp(request)).isEqualTo("10.0.0.1");
        }

        @Test
        @DisplayName("X-Forwarded-For 값이 'unknown'이면 다음 헤더로 폴백한다")
        void fallsBackWhenUnknown() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
            when(request.getHeader("Proxy-Client-IP")).thenReturn("10.10.10.10");

            assertThat(IpUtil.getClientIp(request)).isEqualTo("10.10.10.10");
        }
    }

    @Nested
    @DisplayName("Fallback 처리")
    class Fallback {

        @Test
        @DisplayName("모든 헤더가 없으면 RemoteAddr를 반환한다")
        void returnsRemoteAddrWhenNoHeaders() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            // 모든 헤더 null 반환
            when(request.getHeader(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            assertThat(IpUtil.getClientIp(request)).isEqualTo("127.0.0.1");
        }

        @Test
        @DisplayName("헤더 값이 빈 문자열이면 RemoteAddr로 폴백한다")
        void fallsBackOnEmptyHeader() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader(org.mockito.ArgumentMatchers.anyString())).thenReturn("");
            when(request.getRemoteAddr()).thenReturn("192.0.2.1");

            assertThat(IpUtil.getClientIp(request)).isEqualTo("192.0.2.1");
        }
    }
}
