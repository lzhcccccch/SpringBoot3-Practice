package com.lzhch.practice.easyrule.rules;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

/**
 * 自定义规则1
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/7/27 16:04
 */

@Slf4j
@Rule(name = "MyRuleOne", description = "自定义规则1", priority = 1)
public class MyRuleOne {

    // @Condition 为规则的条件. 入参可以使用@Fact进行标注, 可一个可多个; 也可使用Facts进行传参(参考MyRuleOne-MyRuleThree)
    @Condition
    public boolean validFactoryCode(@Fact(value = "factoryCode") String factoryCode) {
        return "8310".equals(factoryCode) || "80K0".equals(factoryCode);
    }

    @Action
    public void then(Facts facts) {
        log.info("自定义规则1成功 :{}", facts.toString());
    }

}
