package com.example.lib.web.core.strategy;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.common.core.exception.BaseSystemException;
import com.example.lib.common.core.exception.ErrorType;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.error.SystemExceptionStrategy;
import com.example.lib.web.core.strategy.warn.DomainExceptionStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ExceptionHandleStrategy 단위 테스트")
class ExceptionHandleStrategyTest {

    // --- 픽스처 ---
    private static final ErrorType DOMAIN_TYPE = new ErrorType() {
        @Override public int getStatus() { return 404; }
        @Override public String getCode() { return "D404"; }
        @Override public String getMessage() { return "도메인 항목 없음"; }
    };

    private static final ErrorType SYSTEM_TYPE = new ErrorType() {
        @Override public int getStatus() { return 503; }
        @Override public String getCode() { return "S503"; }
        @Override public String getMessage() { return "서비스 불가"; }
    };

    static class TestDomainEx extends BaseDomainException {
        TestDomainEx() { super(DOMAIN_TYPE); }
        TestDomainEx(Object details) { super(DOMAIN_TYPE, details); }
    }

    static class TestSystemEx extends BaseSystemException {
        TestSystemEx() { super(SYSTEM_TYPE); }
    }

    @Nested
    @DisplayName("DomainExceptionStrategy")
    class DomainStrategyTest {

        private final DomainExceptionStrategy strategy = new DomainExceptionStrategy();

        @Test
        @DisplayName("BaseDomainException에 대해 supports=true 반환")
        void supportsDomainException() {
            assertThat(strategy.supports(new TestDomainEx())).isTrue();
        }

        @Test
        @DisplayName("일반 RuntimeException에 대해 supports=false 반환")
        void doesNotSupportRuntimeException() {
            assertThat(strategy.supports(new RuntimeException())).isFalse();
        }

        @Test
        @DisplayName("handle()은 ErrorType의 status와 code를 ApiError에 담는다")
        void handleReturnsCorrectApiError() {
            ApiError error = strategy.handle(new TestDomainEx(), "/api/domain");

            assertThat(error.status()).isEqualTo(404);
            assertThat(error.code()).isEqualTo("D404");
            assertThat(error.instance()).isEqualTo("/api/domain");
        }

        @Test
        @DisplayName("details가 있으면 ApiError.details에 포함된다")
        void handleIncludesDetails() {
            ApiError error = strategy.handle(new TestDomainEx("상세 정보"), "/api/test");

            assertThat(error.details()).isEqualTo("상세 정보");
        }
    }

    @Nested
    @DisplayName("SystemExceptionStrategy")
    class SystemStrategyTest {

        private final SystemExceptionStrategy strategy = new SystemExceptionStrategy();

        @Test
        @DisplayName("BaseSystemException에 대해 supports=true 반환")
        void supportsSystemException() {
            assertThat(strategy.supports(new TestSystemEx())).isTrue();
        }

        @Test
        @DisplayName("BaseDomainException에 대해 supports=false 반환")
        void doesNotSupportDomainException() {
            assertThat(strategy.supports(new TestDomainEx())).isFalse();
        }

        @Test
        @DisplayName("handle()은 503 상태 코드와 S503 코드를 반환한다")
        void handleReturnsCorrectApiError() {
            ApiError error = strategy.handle(new TestSystemEx(), "/api/system");

            assertThat(error.status()).isEqualTo(503);
            assertThat(error.code()).isEqualTo("S503");
        }
    }

    @Nested
    @DisplayName("DefaultExceptionStrategy")
    class DefaultStrategyTest {

        private final DefaultExceptionStrategy strategy = new DefaultExceptionStrategy();

        @Test
        @DisplayName("모든 예외에 대해 supports=true 반환 (fallback)")
        void supportsAllExceptions() {
            assertThat(strategy.supports(new RuntimeException())).isTrue();
            assertThat(strategy.supports(new TestDomainEx())).isTrue();
            assertThat(strategy.supports(new IllegalArgumentException())).isTrue();
        }

        @Test
        @DisplayName("handle()은 항상 500 INTERNAL_SERVER_ERROR를 반환한다")
        void handleReturnsInternalServerError() {
            ApiError error = strategy.handle(new RuntimeException("예상 못한 에러"), "/api/fallback");

            assertThat(error.status()).isEqualTo(500);
            assertThat(error.code()).isEqualTo("G500");
            assertThat(error.instance()).isEqualTo("/api/fallback");
            assertThat(error.details()).isEqualTo("예상 못한 에러");
        }
    }
}
