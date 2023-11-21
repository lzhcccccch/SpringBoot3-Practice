package com.lzhch.practice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import com.lzhch.practice.service.IParamGroupValidatedService;
import com.lzhch.practice.validatedtype.ParamGroupValidated;
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
// 在接口上添加 @Validated 注解, 对该类进行参数校验
@Validated
public class ParamGroupValidatedServiceImpl implements IParamGroupValidatedService {

    /**
     * 在方法的参数上可以直接使用 @NotBlank 等注解
     */
    @Override
    public void filedValidated(String username) {
        log.info("service username :{}", username);
    }

    /**
     * 不分组校验
     * 在方法上使用 @Valid 注解, 采用默认分组(实体也不指定分组)
     * 注意:接口中方法参数必须要加 @Valid 注解; 实现类中可加可不加
     *
     * @param paramGroupValidatedReq param
     * @return: void
     * Author: lzhch 2023/11/21 14:56
     * Since: 1.0.0
     */
    @Override
    public void create(@Valid ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("service result :{}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 分组校验
     * 在不分组校验的基础上对方法添加使用 @Validated 注解, 并指定分组
     * 注意:接口中方法参数必须要加 @Valid 注解; @Validated 可在接口中也可在实现类中; 不能只在实现类中添加两个注解
     *
     * @param paramGroupValidatedReq param
     * @return: void
     * Author: lzhch 2023/11/21 14:55
     * Since: 1.0.0
     */
    @Override
    @Validated(value = ParamGroupValidated.Create.class)
    public void create1(ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("service result :{}", JSON.toJSONString(paramGroupValidatedReq));
    }

}
