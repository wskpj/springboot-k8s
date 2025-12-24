package com.example.lib.web.core.dispatcher;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.common.core.exception.BaseSystemException;
import com.example.lib.common.core.exception.ErrorType;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.DefaultExceptionStrategy;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.web.core.strategy.error.SystemExceptionStrategy;
import com.example.lib.web.core.strategy.warn.DomainExceptionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorDispatcher 테스트")
class ErrorDispatcherTest {

    // --- 테스트용 픽스처 ---
    enum TestErrorType implements ErrorType {
        DOMAIN_ERROR(400, "DOMAIN_001", "도메인 에러"),
        SYSTEM_ERROR(500, "SYSTEM_001", "시스템 에러"),
        UNKNOWN_ERROR(500, "UNKNOWN", "알 수 없는 에러");

        private final int status;
        private final String code;
        private final String message;

        TestErrorType(int status, String code, String message) {
            this.status = status; this.code = code; this.message = message;
        }

        @Override public int getStatus() { return status; }
        @Override public String getCode() { return code; }
        @Override public String getMessage() { return message; }
    }

    static class TestDomainException extends BaseDomainException {
        TestDomainException() { super(TestErrorType.DOMAIN_ERROR); }
        TestDomainException(Object details) { super(TestErrorType.DOMAIN_ERROR, details); }
    }

    static class TestSystemException extends BaseSystemException {
        TestSystemException() { super(TestErrorType.SYSTEM_ERROR); }
    }

    private ErrorDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        List<ExceptionHandleStrategy<?>> strategies = List.of(
            new DomainExceptionStrategy(),
            new SystemExceptionStrategy()
        );
        dispatcher = new ErrorDispatcher(strategies, new DefaultExceptionStrategy());
    }

    @Nested
    @DisplayName("전략 선택 (Strategy Resolution)")
    class StrategyResolution {

        @Test
        @DisplayName("BaseDomainException은 DomainExceptionStrategy로 처리된다")
        void dispatchesDomainException() {
            ApiError error = dispatcher.dispatch(new TestDomainException(), "/api/test");

            assertThat(error.status()).isEqualTo(400);
            assertThat(error.code()).isEqualTo("DOMAIN_001");
            assertThat(error.instance()).isEqualTo("/api/test");
        }

        @Test
        @DisplayName("BaseSystemException은 SystemExceptionStrategy로 처리된다")
        void dispatchesSystemException() {
            ApiError error = dispatcher.dispatch(new TestSystemException(), "/api/test");

            assertThat(error.status()).isEqualTo(500);
            assertThat(error.code()).isEqualTo("SYSTEM_001");
        }

        @Test
        @DisplayName("알 수 없는 예외는 DefaultExceptionStrategy(fallback)으로 처리된다")
        void dispatchesToFallback() {
            ApiError error = dispatcher.dispatch(new RuntimeException("unexpected"), "/api/unknown");

            assertThat(error.status()).isEqualTo(500);
            assertThat(error.code()).isEqualTo("INTERNAL_SERVER_ERROR");
        }
    }

    @Nested
    @DisplayName("details 전달")
    class DetailsForwarding {

        @Test
        @DisplayName("예외에 details가 있으면 ApiError에 포함된다")
        void includesDetailsInApiError() {
            ApiError error = dispatcher.dispatch(new TestDomainException("추가 정보"), "/api/test");

            assertThat(error.details()).isEqualTo("추가 정보");
        }

        @Test
        @DisplayName("details가 없으면 ApiError.details는 null이다")
        void nullDetailsWhenNotProvided() {
            ApiError error = dispatcher.dispatch(new TestDomainException(), "/api/test");

            assertThat(error.details()).isNull();
        }
    }
}
