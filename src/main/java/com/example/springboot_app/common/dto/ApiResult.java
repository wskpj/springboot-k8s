package com.example.springboot_app.common.dto;

import com.example.springboot_app.global.error.HttpError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private final boolean success;
    private final T data;
    private final HttpError error;

    private ApiResult(boolean success, T data, HttpError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static <T> ApiResult<T> fail(HttpError error) {
        return new ApiResult<>(false, null, error);
    }
}
