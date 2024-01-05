package com.lzhch.practice.dynamic.annotation;

import com.lzhch.practice.dynamic.config.DataSourceType;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/6 11:23
 */

//  * @Inherited 注解的作用是：允许子类继承父类中的注解。
//  *  添加在类上, 子类会继承该注解, 即父类上添加了 MultiDataSourceTransactional 注解, 子类会默认继承该注解;
//  *  添加在接口上, 实现类不会继承该注解;
//  *  添加在方法上, 子类不会继承该注解
//  */
// @Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSource {

    String value() default DataSourceType.MASTER;

}
