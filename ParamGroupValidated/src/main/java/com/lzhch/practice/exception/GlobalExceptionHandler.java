package com.lzhch.practice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 17:51
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Controller 参数校验
     *
     * @param e MethodArgumentNotValidException: controller 层参数校验失败异常类型
     * @return String: 可返回系统统一返回包装类
     * Author: lzhch 2023/5/10 15:43
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (Objects.isNull(fieldError)) {
            return e.getMessage();
        }

        return fieldError.getDefaultMessage();
    }

}
