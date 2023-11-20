package com.lzhch.practice.service;

import com.lzhch.practice.dto.req.ParamGroupValidatedReq;
import jakarta.validation.Valid;

/**
 * 分组校验接口
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 18:16
 */

public interface IParamGroupValidatedService {

    void create(@Valid ParamGroupValidatedReq paramGroupValidatedReq);

    void create1(@Valid ParamGroupValidatedReq paramGroupValidatedReq);

}
