package com.zsl.bookai.controller;


import com.zsl.bookai.model.ApiResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public abstract class BaseController {
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(1, null, data);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(0, message, null);
    }

    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static String getUsername() {
        var res = RequestContextHolder.getRequestAttributes().getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        return res == null ? null : (String) res;
    }
}
