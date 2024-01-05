package com.lzhch.practice.dynamic.transaction.jta;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lzhch.practice.dynamic.config.DataSourceType;
import com.lzhch.practice.dynamic.config.DruidCommonProperties;
import com.lzhch.practice.dynamic.config.DynamicDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源集成 JTA 配置
 * 只适用于 mapper 对应了 XML 的情况, 需要手写 SQL, 后续优化相关配置项
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/1/4 18:23
 */

@Configuration
public class JtaDataSourceConfig {

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
        DruidXADataSource dataSource = druidCommonProperties.XADataSource(DruidDataSourceBuilder.create().build());
        dataSource.setUrl("jdbc:mysql://datasource?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&allowMultiQueries=true");
        dataSource.setUsername("test");
        dataSource.setPassword("G678w2@test");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSourceBean.setUniqueResourceName("master-xa");
        atomikosDataSourceBean.setXaDataSource(dataSource);
        return atomikosDataSourceBean;
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
        DruidXADataSource dataSource = druidCommonProperties.XADataSource(DruidDataSourceBuilder.create().build());
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&allowMultiQueries=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root#test!");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSourceBean.setUniqueResourceName("slave-xa");
        atomikosDataSourceBean.setXaDataSource(dataSource);
        return atomikosDataSourceBean;
    }

    /***
     *  进行多数据源设置
     *
     * @return DynamicDataSource
     * Author: lzhch 2023/12/6 17:38
     * Since: 1.0.0
     */
    @Bean
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER, masterDataSource());
        if ("true".equals(slaveEnabled)) {
            targetDataSources.put(DataSourceType.SLAVE, slaveDataSource());
        }
        return new DynamicDataSource(masterDataSource(), targetDataSources);
    }

    /**
     * 设置 SqlSessionFactory
     * 在 CustomSqlSessionTemplate 进行 factory 的切换实现多数据源事务
     * sqlSessionTemplate 与 Spring 事务管理一起使用，以确保使用的实际 SqlSession 是与当前 Spring 事务关联的,
     * 此外它还管理会话生命周期，包括根据 Spring 事务配置根据需要关闭，提交或回滚会话
     */
    @Bean
    public CustomSqlSessionTemplate initSqlSessionTemplate() throws Exception {
        Map<Object, SqlSessionFactory> targetSqlSessionFactory = getSqlSessionFactory();
        CustomSqlSessionTemplate sql = new CustomSqlSessionTemplate(targetSqlSessionFactory.get(DataSourceType.MASTER));
        sql.setTargetSqlSessionFactoryMap(targetSqlSessionFactory);
        sql.setDefaultTargetSqlSessionFactory(targetSqlSessionFactory.get(DataSourceType.MASTER));
        return sql;
    }

    /**
     * 设置 SqlSessionFactory
     */
    private Map<Object, SqlSessionFactory> getSqlSessionFactory() throws Exception {
        Map<Object, Object> targetSource = new HashMap<>();
        targetSource.put(DataSourceType.MASTER, masterDataSource());
        targetSource.put(DataSourceType.SLAVE, slaveDataSource());
        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        String targetSources = DataSourceType.MASTER + "," + DataSourceType.SLAVE;
        for (String key : targetSources.split(",")) {
            // SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            // 注意这里使用的是mybatis-plus，所以需要使用 MybatisSqlSessionFactoryBean 来代替 SqlSessionFactoryBean
            // 如果使用 SqlSessionFactoryBean, 则无法对 mybatis-plus 的方法进行事务管理
            MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
            mybatisSqlSessionFactoryBean.setDataSource((DataSource) targetSource.get(key));
            String mappingPath = "classpath*:mapper/**/*Mapper.xml";
            if (StrUtil.isNotBlank(mappingPath)) {
                org.springframework.core.io.Resource[] local = new PathMatchingResourcePatternResolver().getResources(mappingPath.replace("classpath*:**", "classpath:"));
                mybatisSqlSessionFactoryBean.setMapperLocations(local);
            }

            // 如果在使用时有和原生 mybatis-plus 不一致的情况, 在这里对每个数据源进行单独设置
            MybatisConfiguration configuration = new MybatisConfiguration();
            //配置驼峰命名
            configuration.setMapUnderscoreToCamelCase(true);
            //配置sql日志
            configuration.setLogImpl(StdOutImpl.class);
            mybatisSqlSessionFactoryBean.setConfiguration(configuration);
            mybatisSqlSessionFactoryBean.setGlobalConfig(new GlobalConfig());
            mybatisSqlSessionFactoryBean.setPlugins();

            // sqlSessionFactoryMap.put(key, mybatisSqlSessionFactoryBean.getObject());
            sqlSessionFactoryMap.put(key, mybatisSqlSessionFactoryBean.getObject());
        }
        return sqlSessionFactoryMap;
    }

}
