package com.lzhch.practice.dynamic.config;

/**
 * 多数据源类别
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/6 11:23
 */

public interface DataSourceType {

    /**
     * 主库
     */
    String MASTER = "master";

    /**
     * 从库
     */
    String SLAVE = "slave";

}
