package com.example.lib.web.core.response;

import com.example.lib.common.core.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResult 테스트")
class ApiResultTest {

    private static final ErrorType TEST_ERROR_TYPE = new ErrorType() {
        @Override public int getStatus() { return 400; }
        @Override public String getCode() { return "TEST_400"; }
        @Override public String getMessage() { return "테스트 에러"; }
    };

    @Nested
    @DisplayName("ok() - 성공 응답")
    class Ok {

        @Test
        @DisplayName("success=true, data 포함, error=null 로 생성된다")
        void createsSuccessResult() {
            ApiResult<String> result = ApiResult.ok("hello", "trace-123");

            assertThat(result.success()).isTrue();
            assertThat(result.data()).isEqualTo("hello");
            assertThat(result.traceId()).isEqualTo("trace-123");
            assertThat(result.timestamp()).isNotNull();
            assertThat(result.error()).isNull();
        }

        @Test
        @DisplayName("data가 null이어도 성공 응답을 생성할 수 있다")
        void allowsNullData() {
            ApiResult<Void> result = ApiResult.ok(null, "trace-abc");

            assertThat(result.success()).isTrue();
            assertThat(result.data()).isNull();
        }
    }

    @Nested
    @DisplayName("fail() - 실패 응답")
    class Fail {

        @Test
        @DisplayName("success=false, error 포함, data=null 로 생성된다")
        void createsFailureResult() {
            ApiError error = ApiError.of(TEST_ERROR_TYPE, "/api/test", null);
            ApiResult<Object> result = ApiResult.fail(error, "trace-456");

            assertThat(result.success()).isFalse();
            assertThat(result.data()).isNull();
            assertThat(result.traceId()).isEqualTo("trace-456");
            assertThat(result.timestamp()).isNotNull();
            assertThat(result.error()).isEqualTo(error);
        }

        @Test
        @DisplayName("error 내용이 ApiResult에 그대로 유지된다")
        void preservesErrorContent() {
            ApiError error = ApiError.of(TEST_ERROR_TYPE, "/api/fail", "추가 정보");
            ApiResult<Object> result = ApiResult.fail(error, "trace-789");

            assertThat(result.error().code()).isEqualTo("TEST_400");
            assertThat(result.error().status()).isEqualTo(400);
            assertThat(result.error().details()).isEqualTo("추가 정보");
        }
    }
}
