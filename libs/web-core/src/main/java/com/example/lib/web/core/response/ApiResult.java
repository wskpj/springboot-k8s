package com.example.lib.web.core.response;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API 정상 및 에러 응답을 감싸는 불변 객체(Record)입니다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
    boolean success,
    OffsetDateTime timestamp,
    String traceId,
    T data,
    ApiError error
) {
    public static <T> ApiResult<T> ok(T data, String traceId) {
        return new ApiResult<>(true, OffsetDateTime.now(), traceId, data, null);
    }

    public static <T> ApiResult<T> fail(ApiError error, String traceId) {
        return new ApiResult<>(false, OffsetDateTime.now(), traceId, null, error);
    }
}
