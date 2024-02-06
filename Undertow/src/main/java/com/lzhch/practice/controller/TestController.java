package com.lzhch.practice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 测试
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/2/5 16:00
 */

@Slf4j
@RestController
@RequestMapping(value = "undertow")
public class TestController {

    @GetMapping(value = "test")
    public void test(int index) throws InterruptedException {
        log.info("{} 接受到请求 : index = {}", Thread.currentThread().getName(), index);
        TimeUnit.HOURS.sleep(1);
    }

}
