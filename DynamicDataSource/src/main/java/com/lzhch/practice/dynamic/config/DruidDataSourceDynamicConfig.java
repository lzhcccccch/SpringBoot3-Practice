package com.lzhch.practice.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import jakarta.annotation.Resource;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里巴巴 Druid 数据源动态的配置
 * 适合数据源不确定或者经常变动的情况
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/5 15:03
 */

@Configuration
public class DruidDataSourceDynamicConfig {

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
    @Bean(name = "masterDataSource", initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return druidCommonProperties.dataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 进行多数据源设置
     *
     * @return DynamicDataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource() throws SQLException {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER, masterDataSource());

        // 获取Druid配置的从数据库映射Map
        Map<String, DataSourceProperties> allDataSourcesMap = druidCommonProperties.getSlaveDataSourcesMap();

        // 遍历从数据库映射Map
        for (Map.Entry<String, DataSourceProperties> entry : allDataSourcesMap.entrySet()) {

            // 获取键值
            String key = entry.getKey();
            DataSourceProperties value = entry.getValue();

            // 创建Druid数据源对象
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();

            // 设置数据源名称
            druidDataSource.setName(key);

            // 设置数据库连接URL
            druidDataSource.setUrl(value.getUrl());

            // 设置用户名
            druidDataSource.setUsername(value.getUsername());

            // 设置密码
            druidDataSource.setPassword(value.getPassword());

            // 设置驱动类名
            druidDataSource.setDriverClassName(value.getDriverClassName());

            // 将数据源添加到DruidCommonProperties中
            druidCommonProperties.dataSource(druidDataSource);

            // 初始化数据源
            druidDataSource.init();

            // 将数据源添加到targetDataSources中
            targetDataSources.put(key, druidDataSource);
        }

        // 返回动态数据源
        return new DynamicDataSource(masterDataSource(), targetDataSources);
    }

    /**
     * druid 监控配置
     *
     * @return ServletRegistrationBean<StatViewServlet>
     * Author: lzhch 2023/8/17 15:32
     * Since: 1.0.0
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        // 配置StatViewServlet ServletRegistrationBean
        ServletRegistrationBean<StatViewServlet> srb =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // 设置控制台管理用户
        srb.addInitParameter("loginUsername", "root");
        srb.addInitParameter("loginPassword", "Haier,123");

        // 是否可以重置数据
        srb.addInitParameter("resetEnable", "false");

        // 返回ServletRegistrationBean
        return srb;
    }


    /**
     * 配置监控过滤器
     *
     * @return FilterRegistrationBean<WebStatFilter>
     * Author: lzhch 2023/8/17 15:32
     * Since: 1.0.0
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
        // 创建一个FilterRegistrationBean<WebStatFilter>的bean，并指定使用的过滤器为WebStatFilter
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
        // 添加过滤规则，设置为所有的url都使用该过滤器
        bean.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息，指定一些文件格式不需要经过该过滤器处理，以逗号分隔
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        // 返回创建的bean
        return bean;
    }


    /**
     * 创建一个DruidStatInterceptor的bean
     *
     * @return 返回DruidStatInterceptor对象
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }


    /**
     * 配置一个基于正则表达式的切面点（Method）的切点对象（Pointcut）
     *
     * @return 切点对象（Pointcut）
     */
    @Bean
    @Scope("prototype")
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        // 设置切面应用的包名正则表达式
        pointcut.setPatterns("com.test.*", "com.test1.*");
        return pointcut;
    }


    /**
     * 配置druid的spring监控页面
     *
     * @param druidStatInterceptor 拦截器
     * @param druidStatPointcut    切点
     * @return DefaultPointcutAdvisor
     * Author: lzhch 2023/8/17 17:01
     * Since: 1.0.0
     */
    @Bean
    public DefaultPointcutAdvisor druidStatAdvisor(DruidStatInterceptor druidStatInterceptor, JdkRegexpMethodPointcut druidStatPointcut) {
        // 创建一个DefaultPointcutAdvisor对象
        DefaultPointcutAdvisor defaultPointAdvisor = new DefaultPointcutAdvisor();

        // 设置DefaultPointcutAdvisor对象的pointcut属性为druidStatPointcut
        defaultPointAdvisor.setPointcut(druidStatPointcut);

        // 设置DefaultPointcutAdvisor对象的advice属性为druidStatInterceptor
        defaultPointAdvisor.setAdvice(druidStatInterceptor);

        // 返回创建的DefaultPointcutAdvisor对象
        return defaultPointAdvisor;
    }

}
