package com.lzhch.practice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
     * Controller 层参数校验
     *
     * @param methodArgumentNotValidException: Controller 层参数校验失败异常类型
     * @return 统一封装的结果类, 含有代码code和提示信息msg
     * Author: lzhch 2023/11/21 15:13
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error(methodArgumentNotValidException.getMessage(), methodArgumentNotValidException);
        FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
        if (Objects.isNull(fieldError)) {
            return methodArgumentNotValidException.getMessage();
        }

        return fieldError.getDefaultMessage();
    }

    /**
     * 捕获并处理未授权异常
     *
     * @param e: Service 层参数校验失败异常类型
     * @return 统一封装的结果类, 含有代码code和提示信息msg
     * Author: lzhch 2023/11/21 15:13
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        return String.join(";", e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessageTemplate)
                .toList());
    }

}
