package com.lzhch.practice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/5 11:02
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DynamicDataSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDataSourceApplication.class, args);
        System.out.println("--------------- Hello world! -----------------");
    }

}