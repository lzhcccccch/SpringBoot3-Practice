package com.lzhch.practice.dynamic.aspect;

import com.lzhch.practice.dynamic.config.DataSourceType;
import com.lzhch.practice.dynamic.DynamicDataSourceContextHolder;
import com.lzhch.practice.dynamic.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面, 进行数据源的切换
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/6 11:25
 */

@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    /*
     * @annotation匹配指定注解的方法
     * @within匹配指定注解的类
     * 注意：这里只拦截所注解的类，如果调用的是父类的方法，那么不会拦截，除非父类方法在子类中被覆盖。
     */
    @Pointcut("@annotation(com.lzhch.practice.dynamic.annotation.DataSource) || @within(com.lzhch.practice.dynamic.annotation.DataSource)")
    public void dataSourcePointCut() {}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> dataClass = Class.forName(signature.getDeclaringTypeName());

        DataSource dsMethod = method.getAnnotation(DataSource.class);
        DataSource dsClass = dataClass.getAnnotation(DataSource.class);
        if(dsMethod != null){
            //方法优先，如果方法上存在注解，则优先使用方法上的注解
            DynamicDataSourceContextHolder.push(dsMethod.value());
            log.debug("method first, set datasource is " + dsMethod.value());
        }else if(dsClass != null){
            //其次类优先，如果类上存在注解，则使用类上的注解
            DynamicDataSourceContextHolder.push(dsClass.value());
            log.debug("class second, set datasource is " + dsClass.value());
        }else{
            //如果都不存在，则使用默认
            DynamicDataSourceContextHolder.push(DataSourceType.MASTER);
            log.debug("default, set datasource is " + DataSourceType.MASTER);
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clear();
            log.debug("clean datasource");
        }
    }

}
