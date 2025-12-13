package com.example.springboot_app.api.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private ApiResult(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static <T> ApiResult<T> fail(ErrorResponse error) {
        return new ApiResult<>(false, null, error);
    }
}
