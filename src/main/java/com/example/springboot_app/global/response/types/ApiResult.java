package com.example.springboot_app.global.response.types;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private final boolean success;
    private final String timestamp;
    private final String traceId;
    private final T data;
    private final ApiError error;

    private ApiResult(boolean success, String traceId, T data, ApiError error) {
        this.success = success;
        this.timestamp = OffsetDateTime.now().toString();
        this.traceId = traceId;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data, String traceId) {
        return new ApiResult<>(true, traceId, data, null);
    }

    public static <T> ApiResult<T> fail(ApiError error, String traceId) {
        return new ApiResult<>(false, traceId, null, error);
    }
}
