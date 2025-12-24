package com.example.lib.web.core.response;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API 정상 및 에러 응답을 감싸는 순수 데이터 객체(Record)입니다.
 * 상태를 직접 조회하지 않으며, 생성 시점에 필요한 모든 정보를 주입받습니다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
    boolean success,
    OffsetDateTime timestamp,
    String traceId,
    T data,
    ApiError error
) {
    /**
     * 정상 응답 객체를 생성합니다.
     */
    public static <T> ApiResult<T> ok(T data, String traceId) {
        return new ApiResult<>(true, OffsetDateTime.now(), traceId, data, null);
    }

    /**
     * 에러 응답 객체를 생성합니다.
     */
    public static <T> ApiResult<T> fail(ApiError error, String traceId) {
        return new ApiResult<>(false, OffsetDateTime.now(), traceId, null, error);
    }
}
