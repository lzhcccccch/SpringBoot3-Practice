package com.lzhch.practice.dynamic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里巴巴 Druid 数据源的固定配置
 * 适合数据源确定且不变的情况
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/15 17:53
 */

@Configuration
public class DruidDataSourceFixedConfig {

    @Value(value = "${spring.datasource.druid.slave.enable}")
    private String slaveEnabled;

    @Resource
    private DruidCommonProperties druidCommonProperties;

    /***
     *  设置主数据源
     *  initMethod = "init", 其中 init 调用 DruidDataSource 中的 init 方法;
     *  指定该属性, 可在应用启动时控制台看到初始化日志; 若不指定, 则在使用时进行初始化, 且不会打印初始化日志.
     *
     * @return DataSource
     * Author: lzhch 2023/12/6 17:36
     * Since: 1.0.0
     */
    @Primary
    @Bean(name = "masterDataSource", initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        // return druidCommonProperties.dataSource(DruidDataSourceBuilder.create().build());
        return druidCommonProperties.XADataSource();
    }

    /**
     * 设置从数据源
     * 当 spring.datasource.druid.slave.enable 为 true 时开启从数据源
     * {@link #slaveEnabled}
     *
     * @return DataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean(name = "slaveDataSource", initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enable", havingValue = "true")
    public DataSource slaveDataSource() {
        // return DruidDataSourceBuilder.create().build();
        // 使用 XA 控制多数据源事务
        return druidCommonProperties.XADataSource();
    }

    /***
     *  进行多数据源设置
     *  指定name, 使用 @Qualifier(value = "dynamicDataSource") 获取
     *
     * @return DynamicDataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER, masterDataSource());
        if ("true".equals(slaveEnabled)) {
            targetDataSources.put(DataSourceType.SLAVE, slaveDataSource());
        }
        return new DynamicDataSource(masterDataSource(), targetDataSources);
    }

    /**
     * 配置事务管理器
     *
     * @return DataSourceTransactionManager
     * Author: lzhch 2023/12/26 16:04
     * Since: 1.0.0
     */
    /*@Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager masterTransactionManager() {
        *//*
         *  如果不进行事务管理器的配置, 在添加了 @Transactional 注解时会导致无法切换数据源;
         *  添加了该配置, 并在需要切换数据源的地方使用 @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) 新开启一个事务, 保证可以正常切换数据源
         *  但是该方式会导致数据不一致, 因为新开启的事务不能和原来的事务保持一致性
         *//*
        // PlatformTransactionManager
        return new DataSourceTransactionManager(masterDataSource());
    }*/

}
