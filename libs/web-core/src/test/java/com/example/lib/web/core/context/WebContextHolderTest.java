package com.example.lib.web.core.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("WebContextHolder 테스트")
class WebContextHolderTest {

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    private void setRequestContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    @Nested
    @DisplayName("getRequest()")
    class GetRequest {

        @Test
        @DisplayName("RequestContext가 없으면 Optional.empty()를 반환한다")
        void returnsEmptyWhenNoContext() {
            Optional<?> result = WebContextHolder.getRequest();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("RequestContext가 있으면 HttpServletRequest를 반환한다")
        void returnsRequestWhenContextSet() {
            setRequestContext();

            assertThat(WebContextHolder.getRequest()).isPresent();
        }
    }

    @Nested
    @DisplayName("getResponse()")
    class GetResponse {

        @Test
        @DisplayName("RequestContext가 없으면 Optional.empty()를 반환한다")
        void returnsEmptyWhenNoContext() {
            assertThat(WebContextHolder.getResponse()).isEmpty();
        }

        @Test
        @DisplayName("RequestContext가 있으면 HttpServletResponse를 반환한다")
        void returnsResponseWhenContextSet() {
            setRequestContext();

            assertThat(WebContextHolder.getResponse()).isPresent();
        }
    }

    @Nested
    @DisplayName("getRequiredRequest() / getRequiredResponse()")
    class Required {

        @Test
        @DisplayName("RequestContext가 없으면 getRequiredRequest()는 IllegalStateException을 던진다")
        void throwsWhenRequestNotAvailable() {
            assertThatThrownBy(WebContextHolder::getRequiredRequest)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("HTTP Request is not available");
        }

        @Test
        @DisplayName("RequestContext가 없으면 getRequiredResponse()는 IllegalStateException을 던진다")
        void throwsWhenResponseNotAvailable() {
            assertThatThrownBy(WebContextHolder::getRequiredResponse)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("HTTP Response is not available");
        }

        @Test
        @DisplayName("RequestContext가 있으면 getRequiredRequest()는 예외 없이 반환한다")
        void returnsRequestWhenAvailable() {
            setRequestContext();

            assertThat(WebContextHolder.getRequiredRequest()).isNotNull();
        }
    }
}
