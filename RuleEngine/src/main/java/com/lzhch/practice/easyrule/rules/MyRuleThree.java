package com.lzhch.practice.easyrule.rules;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

/**
 * 自定义规则3
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/7/27 16:04
 */

@Slf4j
@Rule(name = "MyRuleThree", description = "自定义规则3", priority = 3)
public class MyRuleThree {

    @Condition
    public boolean when(Facts facts) {
        return "8310".equals(facts.get("factoryCode")) && "36".equals(facts.get("lineCode"));
    }

    @Action
    public void thenStep1(Facts facts) {
        System.out.println("自定义规则3-第1步成功" + facts.toString());
    }

    @Action(order = 1)
    public void thenStep2(Facts facts) {
        log.info("自定义规则3-第2步成功 :{}", facts.toString());
    }

}
