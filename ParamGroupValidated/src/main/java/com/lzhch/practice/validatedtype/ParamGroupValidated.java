package com.lzhch.practice.validatedtype;

import jakarta.validation.groups.Default;

/**
 * 新增参数校验接口
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 17:20
 */

public interface ParamGroupValidated {

    /**
     * 在声明分组的时候加上 extend javax.validation.groups.Default
     * 否则, 在你声明 @Validated(Update.class)的时候, 就会出现你在默认没添加 groups = {} 的时候
     * 校验组 @Email(message = "邮箱格式不对") 会不去校验, 因为默认的校验组是 groups = {Default.class}.
     */

    interface Create extends Default {
    }

    interface Update extends Default {
    }

}
