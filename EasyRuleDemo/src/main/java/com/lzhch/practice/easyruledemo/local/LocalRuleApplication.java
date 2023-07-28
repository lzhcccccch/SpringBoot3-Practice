package com.lzhch.practice.easyruledemo.local;

import com.lzhch.practice.easyruledemo.rules.MyRuleOne;
import com.lzhch.practice.easyruledemo.rules.MyRuleThree;
import com.lzhch.practice.easyruledemo.rules.MyRuleTwo;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.DefaultRulesEngine;

/**
 * 本地规则启动,无需启动spring容器
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/7/27 17:41
 */

public class LocalRuleApplication {
    public static void main(String[] args) {
        // create a rules engine
        // skipOnFirstAppliedRule(true) 在满足一个规则之后, 无论成功与失败, 均跳过后面的规则
        // RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngineParameters parameters = new RulesEngineParameters();
        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);

        Rules rules = new Rules();
        rules.register(new MyRuleOne());
        rules.register(new MyRuleTwo());
        rules.register(new MyRuleThree());

        Facts facts = new Facts();
        facts.put("factoryCode", "8310");
        facts.put("lineCode", "36");

        rulesEngine.fire(rules, facts);

        for (Rule rule : rules) {
            boolean evaluate = rule.evaluate(facts);
            System.out.println(evaluate);
        }
    }

}
