package com.example.lib.web.core.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResultTest {

    @Test
    @DisplayName("성공 응답 객체가 올바르게 생성되는지 확인한다")
    void okTest() {
        String data = "success data";
        String traceId = "trace-123";
        ApiResult<String> result = ApiResult.ok(data, traceId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo(data);
        assertThat(result.getTraceId()).isEqualTo(traceId);
        assertThat(result.getTimestamp()).isNotNull();
        assertThat(result.getError()).isNull();
    }

    @Test
    @DisplayName("실패 응답 객체가 올바르게 생성되는지 확인한다")
    void failTest() {
        ApiError error = ApiError.builder()
                .code("ERR_CODE")
                .message("Error Message")
                .status(400)
                .build();
        String traceId = "trace-456";
        ApiResult<Object> result = ApiResult.fail(error, traceId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getData()).isNull();
        assertThat(result.getTraceId()).isEqualTo(traceId);
        assertThat(result.getError()).isEqualTo(error);
    }
}
