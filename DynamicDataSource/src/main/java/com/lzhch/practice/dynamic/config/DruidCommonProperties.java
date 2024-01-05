package com.lzhch.practice.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * druid 配置
 * <p>
 * Author: lzhch 2023/8/17 15:27
 * Since: 3.0.1 是主库和从库读取一样的配置, 若配置不同可不使用该类
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidCommonProperties {

    /**
     * 初始大小。
     */
    private int initialSize;

    /**
     * 最小空闲连接数。
     */
    private int minIdle;

    /**
     * 最大活动连接数。
     */
    private int maxActive;

    /**
     * 最大等待时间。
     */
    private int maxWait;

    /**
     * 每隔多少毫秒执行一次驱除连接空闲扫描。
     */
    private int timeBetweenEvictionRunsMillis;

    /**
     * 连接可以空闲的最短时间。
     */
    private int minEvictableIdleTimeMillis;

    /**
     * 连接可以空闲的最大时间。
     */
    private int maxEvictableIdleTimeMillis;

    /**
     * 驱逐连接时使用的验证查询。
     */
    private String validationQuery;

    /**
     * 是否在空闲连接被驱逐之前进行验证。
     */
    private boolean testWhileIdle;

    /**
     * 是否在被借出之前进行验证。
     */
    private boolean testOnBorrow;

    /**
     * 是否在返回之前进行验证。
     */
    private boolean testOnReturn;

    /**
     * 是否删除被遗弃的连接。
     */
    private boolean removeAbandoned;

    /**
     * 被遗弃连接的超时时间。
     */
    private int removeAbandonedTimeout;

    /**
     * 是否记录被遗弃的连接。
     */
    private boolean logAbandoned;

    /**
     * 连接套接字超时时间。
     */
    private int socketTimeout;

    /**
     * 从数据源属性集合, new 初始化防止不配置启动报错
     */
    @NestedConfigurationProperty
    private Map<String, DataSourceProperties> slaveDataSourcesMap = new LinkedHashMap<>();

    /**
     * 配置数据源
     *
     * @param datasource 数据源
     * @return DruidDataSource
     */
    public DruidDataSource dataSource(DruidDataSource datasource) {
        // 配置初始化大小、最小、最大
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        // 配置获取连接等待超时的时间
        datasource.setMaxWait(maxWait);

        // 验证空闲连接使用 select 1，而不是使用MySQL的Ping. 解决连接空闲60秒报错问题(ERROR com.alibaba.druid.pool.DruidAbstractDataSource - discard long time none received connection.)
        datasource.setUsePingMethod(false);

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        // 配置一个连接在池中最小、最大生存的时间，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        //  用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
        datasource.setValidationQuery(validationQuery);

        // 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        datasource.setTestWhileIdle(testWhileIdle);

        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        datasource.setTestOnBorrow(testOnBorrow);

        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        datasource.setTestOnReturn(testOnReturn);

        // 是否自动回收超时链接
        datasource.setRemoveAbandoned(removeAbandoned);

        // 超时时间
        datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);

        // 回收时记录日志, 可定位SQL和代码
        datasource.setLogAbandoned(logAbandoned);

        // socket连接超时时间(防止慢SQL查询报CJCommunicationsException: Communications link failure; java.net.SocketTimeoutException: Read timed out)
        datasource.setSocketTimeout(socketTimeout);

        return datasource;
    }

    /***
     *  XA 多数据源事务属性设置
     *
     * @return DruidXADataSource
     * Author: lzhch 2024/1/5 18:15
     * Since: 1.0.0
     */
    public DruidXADataSource XADataSource() {
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        // 将上面方法的属性复制下来即可
        druidXADataSource.setInitialSize(initialSize);
        druidXADataSource.setMaxActive(maxActive);
        druidXADataSource.setMinIdle(minIdle);

        return druidXADataSource;
    }

}
