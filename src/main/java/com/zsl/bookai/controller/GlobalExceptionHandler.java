package com.zsl.bookai.controller;

import com.zsl.bookai.model.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult<Void> handle404(NoHandlerFoundException e) {
        logger.error("404 Not Found: {}", e.getRequestURL());
        return ApiResult.fail("接口不存在: " + e.getRequestURL());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleGeneral(Throwable e) {
        logger.error("Internal error: {}", e.getMessage(), e);
        return ApiResult.fail("请求失败: " + e.getMessage());
    }
}
