package com.lzhch.practice.dynamic.transaction.jta;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lzhch.practice.dynamic.config.DataSourceType;
import com.lzhch.practice.dynamic.config.DynamicDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

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

    /**
     * 设置 SqlSessionFactory
     * 在 CustomSqlSessionTemplate 进行 factory 的切换实现多数据源事务
     * sqlSessionTemplate 与 Spring 事务管理一起使用，以确保使用的实际 SqlSession 是与当前 Spring 事务关联的,
     * 此外它还管理会话生命周期，包括根据 Spring 事务配置根据需要关闭，提交或回滚会话
     */
    @Bean
    public CustomSqlSessionTemplate initSqlSessionTemplate(@Qualifier(value = "dynamicDataSource") DynamicDataSource dynamicDataSource) throws Exception {
        Map<Object, SqlSessionFactory> targetSqlSessionFactory = getSqlSessionFactory(dynamicDataSource);
        CustomSqlSessionTemplate sql = new CustomSqlSessionTemplate(targetSqlSessionFactory.get(DataSourceType.MASTER));
        sql.setTargetSqlSessionFactoryMap(targetSqlSessionFactory);
        sql.setDefaultTargetSqlSessionFactory(targetSqlSessionFactory.get(DataSourceType.MASTER));
        return sql;
    }

    /**
     * 设置 SqlSessionFactory
     */

    private Map<Object, SqlSessionFactory> getSqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        Map<Object, DataSource> targetSource = new HashMap<>();
        Map<Object, DataSource> resolvedDataSources = dynamicDataSource.getResolvedDataSources();
        for (Map.Entry<Object, DataSource> entry : resolvedDataSources.entrySet()){
            AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
                atomikosDataSourceBean.setUniqueResourceName("xa" + entry.getKey());
                // atomikosDataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
                atomikosDataSourceBean.setXaDataSource((DruidXADataSource) entry.getValue());
                // atomikosDataSourceBean.setXaDataSource((DruidXADataSource) slaveDataSource());
            targetSource.put(entry.getKey(), atomikosDataSourceBean);
        }

        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        for (Map.Entry<Object, DataSource> entry : targetSource.entrySet()) {
            // SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            // 注意这里使用的是mybatis-plus，所以需要使用 MybatisSqlSessionFactoryBean 来代替 SqlSessionFactoryBean
            // 如果使用 SqlSessionFactoryBean, 则无法对 mybatis-plus 的方法进行事务管理
            MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
            mybatisSqlSessionFactoryBean.setDataSource(entry.getValue());
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

            sqlSessionFactoryMap.put(entry.getKey(), mybatisSqlSessionFactoryBean.getObject());
        }

        return sqlSessionFactoryMap;
    }

}
