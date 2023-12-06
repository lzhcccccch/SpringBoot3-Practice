package com.lzhch.practice.dynamic.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里巴巴 Druid 数据源配置
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/5 15:03
 */

@Configuration
public class DruidDataSourceConfig {

    @Value(value = "${spring.datasource.druid.slave.enable}")
    private String slaveEnabled;

    /***
     *  设置主数据源
     *  initMethod = "init", 其中 init 调用 DruidDataSource 中的 init 方法;
     *  指定该属性, 可在应用启动时控制台看到初始化日志; 若不指定, 则在使用时进行初始化, 且不会打印初始化日志.
     *
     * @return DataSource
     * Author: lzhch 2023/12/6 17:36
     * Since: 1.0.0
     */
    @Bean(name = "masterDataSource", initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /***
     *  设置从数据源
     *  当 spring.datasource.druid.slave.enable 为 true 时开启从数据源
     *  {@link #slaveEnabled}
     *
     * @return DataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean(name = "slaveDataSource", initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enable", havingValue = "true")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /***
     *  进行多数据源设置
     *
     * @return DynamicDataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER, masterDataSource());
        if ("true".equals(slaveEnabled)) {
            targetDataSources.put(DataSourceType.SLAVE, slaveDataSource());
        }
        return new DynamicDataSource(masterDataSource(), targetDataSources);
    }

}
