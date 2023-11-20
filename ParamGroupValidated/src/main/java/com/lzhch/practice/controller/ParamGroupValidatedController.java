package com.lzhch.practice.controller;

import com.alibaba.fastjson2.JSON;
import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import com.lzhch.practice.validatedtype.CreateParamValidated;
import com.lzhch.practice.validatedtype.UpdateParamValidated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数分组校验 controller
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 17:22
 */

@Slf4j
@RestController
@RequestMapping(value = "validated/paramGroup")
public class ParamGroupValidatedController {

    @PostMapping(value = "create")
    public void create(@RequestBody @Validated(value = CreateParamValidated.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    @PostMapping(value = "update")
    public void update(@RequestBody @Validated(value = UpdateParamValidated.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    @PostMapping(value = "create1")
    public void create1(@RequestBody @Validated(value = ParamGroupValidatedReq.Save.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

    @PostMapping(value = "update1")
    public void update1(@RequestBody @Validated(value = ParamGroupValidatedReq.Update.class) ParamGroupValidatedReq paramGroupValidatedReq) {
        log.info("result {}", JSON.toJSONString(paramGroupValidatedReq));
    }

}
