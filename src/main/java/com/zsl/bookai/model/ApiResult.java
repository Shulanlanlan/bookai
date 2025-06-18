package com.zsl.bookai.model;

import org.springframework.data.relational.core.sql.In;

public class ApiResult<T> {
    public int code;
    public String message;
    public T data;


    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResult(int code, String message) {
        this(code, message, null);
    }

    public ApiResult(int code) {
        this(code, null, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(1, null, data);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(0, message, null);
    }

    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(code, message, null);
    }
}
