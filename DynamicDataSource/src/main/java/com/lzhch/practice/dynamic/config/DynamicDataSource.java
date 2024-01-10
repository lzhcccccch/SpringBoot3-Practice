package com.lzhch.practice.dynamic.config;

import cn.hutool.core.util.StrUtil;
import com.lzhch.practice.dynamic.DynamicDataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/5 15:51
 */

public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 默认数据源
     */
    private final DataSource defaultTargetDataSource;

    /**
     * 所有数据源(包含默认数据源)
     */
    private final Map<Object, Object> targetDataSources;

    /**
     * 设置所有的数据源
     * 构造方法中只完成属性的赋值, 构造方法执行完还有其他操作,所以不进行 Bean 的初始化;
     * 重写 afterPropertiesSet() 方法让 spring 进行 Bean 的初始化
     *
     * @param defaultTargetDataSource 默认数据源
     * @param targetDataSources       所有数据源(包含默认数据源)
     * @return: void
     * Author: lzhch 2023/12/6 16:16
     * Since: 1.0.0
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        this.defaultTargetDataSource = defaultTargetDataSource;
        this.targetDataSources = targetDataSources;
    }

    /**
     * 获取当前数据源的标识
     * 应用通过 ORM 框架(mybatis/JPA等) 和数据库建立链接(Connection), 通过在 AbstractRoutingDataSource 中调用 getConnection 实现
     * 在 getConnection 中最终会调用到 determineCurrentLookupKey 方法, 该方法返回的是数据源的标识
     * 所以多数据源的关键就在于 determineCurrentLookupKey 方法, 多数据源就是建立了多个数据库连接(应用程序和不同数据库的链接)
     * spring 事务就是通过在一个数据库连接中执行全部 SQL 来控制提交和回滚
     * 在多数据源情况下, 每个数据库连接会有自己的事务, 互不影响, 所以这也是多数据源情况下事务失效(异常情况下不能全部回滚)的原因
     *
     * @return Object
     * Author: lzhch 2023/12/6 16:16
     * Since: 1.0.0
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String ds = DynamicDataSourceContextHolder.peek();
        return StrUtil.isBlank(ds) ? DataSourceType.MASTER : ds;
    }

    /**
     * 重写 afterPropertiesSet() 方法让 spring 进行 Bean 的初始化
     */
    public void afterPropertiesSet() {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

}
