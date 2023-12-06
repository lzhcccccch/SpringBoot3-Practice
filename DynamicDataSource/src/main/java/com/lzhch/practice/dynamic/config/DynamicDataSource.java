package com.lzhch.practice.dynamic.config;

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

    /***
     *  设置所有的数据源
     *
     * @param defaultTargetDataSource 默认数据源
     * @param targetDataSources 所有数据源(包含默认数据源)
     * @return: void
     * Author: lzhch 2023/12/6 16:16
     * Since: 1.0.0
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /***
     *  确定当前数据源
     *
     * @return Object
     * Author: lzhch 2023/12/6 16:16
     * Since: 1.0.0
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.peek();
    }

}
