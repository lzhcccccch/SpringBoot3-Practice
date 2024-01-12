package com.lzhch.practice.dynamic.transaction.tx;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多数据源配置
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/1/12 17:33
 */

@Configuration
@MapperScan(value = "com.lzhch.practice.business.mapper")
public class MultiDataSourceConfig {

    @javax.annotation.Resource
    private MybatisPlusProperties mybatisProperties;

    /**
     * 设置 SqlSessionFactory
     */
    @Bean
    @SneakyThrows
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTransactionFactory(new MultiDataSourceTransactionFactory());
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        List<Resource> resourceList = new ArrayList<>();
        for (String mapperLocation : mybatisProperties.getMapperLocations()) {
            resourceList.addAll(Arrays.asList(new PathMatchingResourcePatternResolver().getResources(mapperLocation)));
        }
        Assert.notEmpty(resourceList, "mapperLocations can't be empty");
        sqlSessionFactoryBean.setMapperLocations(resourceList.toArray(new org.springframework.core.io.Resource[resourceList.size()]));

        return sqlSessionFactoryBean.getObject();
    }

}
