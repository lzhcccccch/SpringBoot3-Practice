package com.lzhch.practice;

import cn.hutool.http.HttpUtil;

/**
 * 测试
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/2/5 16:17
 */

public class TestMain {

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                HttpUtil.get("http://localhost:8080/undertow/test?index=" + finalI);
            }).start();
        }
        //阻塞主线程
        Thread.yield();
    }

}
