package com.lzhch.practice.easyrule.rules;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

/**
 * 自定义规则2
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/7/27 16:04
 */

@Slf4j
@Rule(name = "MyRuleTwo", description = "自定义规则2", priority = 2)
public class MyRuleTwo {

    @Condition
    public boolean when(@Fact(value = "factoryCode") String factoryCode, @Fact(value = "lineCode") String lineCode) {
        return "8310".equals(factoryCode) && "36".equals(lineCode);
    }

    @Action
    public void then(Facts facts) {
        log.info("自定义规则2成功 :{}", facts.toString());
    }

}
