package com.lzhch.practice.dynamic.config;

import lombok.Data;

/**
 * 数据源配置
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/15 17:21
 */

@Data
public class DataSourceProperties {

    /**
     * 是否启用: true 启用; false 不启用
     */
    private boolean enable;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 驱动类名
     */
    private String driverClassName;

}
