package com.example.springboot_app.global.response.types;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private final boolean success;
    private final String timestamp;
    private final String requestId;
    private final T data;
    private final ApiError error;

    private ApiResult(boolean success, String requestId, T data, ApiError error) {
        this.success = success;
        this.timestamp = OffsetDateTime.now().toString();
        this.requestId = requestId;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data, String requestId) {
        return new ApiResult<>(true, requestId, data, null);
    }

    public static <T> ApiResult<T> fail(ApiError error, String requestId) {
        return new ApiResult<>(false, requestId, null, error);
    }
}
