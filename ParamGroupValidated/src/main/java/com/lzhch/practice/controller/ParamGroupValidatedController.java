package com.lzhch.practice.controller;

import com.alibaba.fastjson2.JSON;
import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import com.lzhch.practice.service.IParamGroupValidatedService;
import com.lzhch.practice.validatedtype.ParamGroupValidated;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数分组校验 controller
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 17:22
 */

@Slf4j
// 用于在 Controller 层的简单校验, 比如 simple 方法
@Validated
@RestController
@RequestMapping(value = "validated/paramGroup")
public class ParamGroupValidatedController {

    @Resource
    private IParamGroupValidatedService paramGroupValidatedService;

    /**
     * 简单校验
     * 必须在 Controller 上添加 @Validated 注解
     * 指定 groups 可以进行分组校验
     */
    @GetMapping(value = "simple")
    public void simple(@NotBlank(message = "username 不能是空的啊!!!", groups = ParamGroupValidated.Create.class) String username) {
        log.info("result {}", username);
    }

    /**
     * 非 JSON 格式的对象校验
     * 使用 @Validated 注解的 value 属性指定分组
     */
    @GetMapping(value = "simple1")
    public void simple1(@Validated(value = ParamGroupValidated.Create.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 统一接口分组测试新增
     * 使用 @Validated 注解的 value 属性指定分组
     */
    @PostMapping(value = "create")
    public void create(@RequestBody @Validated(value = ParamGroupValidated.Create.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 统一接口分组测试修改
     */
    @PostMapping(value = "update")
    public void update(@RequestBody @Validated(value = ParamGroupValidated.Update.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 内部接口测试分组新增
     */
    @PostMapping(value = "create1")
    public void create1(@RequestBody @Validated(value = ParamGroupValidatedReq.Save.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 内部接口测试分组修改
     */
    @PostMapping(value = "update1")
    public void update1(@RequestBody @Validated(value = ParamGroupValidatedReq.Update.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 测试 Service 分组新增
     */
    @PostMapping(value = "create2")
    public void create2(@RequestBody ParamGroupValidatedReq paramGroupValidatedReq) {
        this.paramGroupValidatedService.create1(paramGroupValidatedReq);
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    /**
     * 测试 Service 字段校验
     */
    @PostMapping(value = "update2")
    public void update2(String username) {
        this.paramGroupValidatedService.filedValidated(username);
        log.info("result {}", username);
    }

}
