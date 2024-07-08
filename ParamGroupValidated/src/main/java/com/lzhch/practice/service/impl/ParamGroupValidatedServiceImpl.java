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

    /*
     * service 实现参数的校验:
     * 1. 实现类上添加 @Validated 注解, 对该类进行参数校验
     * 2. 在接口方法的参数上可以直接使用 @NotBlank/@Valid 等注解, 实现类上添加与否都可以
     *
     * 解释第 2 点:
     * 如果不在接口的方法中添加注解, 则会报错: jakarta.validation.ConstraintDeclarationException: HV000151: A method overriding another method must not redefine the parameter constraint configuration,
     *  but method ParamGroupValidatedServiceImpl#filedValidated(String) redefines the configuration of IParamGroupValidatedService#filedValidated(String).
     * 原因: 这个错误是由于在重写方法时改变了参数的约束配置导致的。
     *  在你的代码中，ParamGroupValidatedServiceImpl 类中的 filedValidated 方法重写了 IParamGroupValidatedService 中的同名方法，
     *  但是改变了参数的约束配置，这违反了 Hibernate Validator 的规则。
     *  为了解决这个问题，你需要确保在重写方法时保持参数的约束配置不变。如果需要改变约束配置，你需要在接口或父类中修改方法的注解，而不是在重写的方法中修改。
     */

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
