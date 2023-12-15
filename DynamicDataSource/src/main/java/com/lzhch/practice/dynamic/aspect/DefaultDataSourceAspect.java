package com.lzhch.practice.dynamic.aspect;

import com.lzhch.practice.dynamic.DynamicDataSourceContextHolder;
import com.lzhch.practice.dynamic.annotation.DataSource;
import com.lzhch.practice.dynamic.config.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 默认数据源切面
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/8 11:11
 */

@Slf4j
@Aspect
@Component
public class DefaultDataSourceAspect {

    /***
     *  默认数据源切点
     *  默认切点: 路径为com.cloud下的任意子包中的任意类名以Controller结尾的类
     *
     * @return: void
     * Author: lzhch 2023/12/8 11:15
     * Since: 3.0.1
     */
    @Pointcut("execution(* com.lzhch.practice.business..*Controller.*(..))")
    public void defaultDataSourcePoint() {
    }

    /**
     * 根据 DataSource 注解即可切换数据源
     */
    @Around("defaultDataSourcePoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getMethod().getName();
        DataSource dataSourceAnnotation = signature.getMethod().getAnnotation(DataSource.class);

        log.debug("=======Aspect Thread :{}", Thread.currentThread().getName());
        if (Objects.nonNull(dataSourceAnnotation)) {
            DynamicDataSourceContextHolder.push(dataSourceAnnotation.value());
            try {
                log.info("{} 方法, 数据源为: {}", methodName, dataSourceAnnotation.value());
                return point.proceed();
            } finally {
                // 不使用 clear 方法, 否则后面的数据源切换会被清空, 造成数据源切换失败
                DynamicDataSourceContextHolder.poll();
                log.info("clean datasource");
            }
        }

        // 根据一定规则设置对应的数据源
        DynamicDataSourceContextHolder.push(DataSourceType.SLAVE);
        // log.info("操作 {} 方法, 数据源: {}", methodName, dataSource);

        try {
            // 执行方法
            // 不使用 clear 方法, 否则后面的数据源切换会被清空, 造成数据源切换失败
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
            log.debug("clean datasource");
        }
    }

}
