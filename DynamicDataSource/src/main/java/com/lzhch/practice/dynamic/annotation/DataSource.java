package com.lzhch.practice.dynamic.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/6 11:23
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    String value() default "";

}
