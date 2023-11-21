package com.lzhch.practice.service;

import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 分组校验接口
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 18:16
 */

public interface IParamGroupValidatedService {

    // 在接口中必须添加 @Valid 以及 @NotBlank 等注解, 否则报错

    /**
     * 字段校验
     */
    void filedValidated(@NotBlank(message = "用户名不能为空") String username);

    /**
     * 不分组校验
     */
    void create(@Valid ParamGroupValidatedReq paramGroupValidatedReq);

    /**
     * 分组校验
     */
    // @Validated(value = ParamGroupValidated.Create.class)
    void create1(@Valid ParamGroupValidatedReq paramGroupValidatedReq);

}
