package com.lzhch.practice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * application
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/2/5 15:59
 */

@SpringBootApplication
public class UndertowApplication {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(UndertowApplication.class, args);
    }

}