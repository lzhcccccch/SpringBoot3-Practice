package com.lzhch.practice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import com.lzhch.practice.service.IParamGroupValidatedService;
import com.lzhch.practice.validatedtype.CreateParamValidated;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 分组校验接口实现类
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 18:16
 */

@Slf4j
@Service
@Validated
public class ParamGroupValidatedServiceImpl implements IParamGroupValidatedService {

    /**
     * 在接口上添加 @Validated 注解, 对该类进行参数校验
     * 在方法的参数上可以直接使用 @NotBlank 等注解
     * <p>
     * 对于对象参数校验的方式:
     * 1. 不分组校验: 在方法上使用 @Valid 注解, 采用默认分组(实体也不指定分组), 参考 create
     * 2. 分组校验在 1 的基础上对方法添加使用 @Validated 注解, 并指定分组, 参考 create1
     * 注意:接口和实现类的方法参数都要加 @Valid 注解
     */

    @Override
    public void create(@Valid ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("service result :{}", JSON.toJSONString(paramGroupValidatedReq));
    }

    @Override
    @Validated(value = CreateParamValidated.class)
    public void create1(@Valid ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("service result :{}", JSON.toJSONString(paramGroupValidatedReq));
    }

}
