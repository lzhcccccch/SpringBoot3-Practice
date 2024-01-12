package com.lzhch.practice.dynamic.transaction.tx;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.lzhch.practice.dynamic.DynamicDataSourceContextHolder;
import com.lzhch.practice.dynamic.config.DataSourceType;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;


/**
 * 多数据源事务管理器
 * <p>
 * spring 原生的事务管理是只会获取一次连接, 并将连接缓存, 第二次获取时直接从缓存中获取
 * 所以导致了切换数据源失效, 因为第二次(不同数据源)并没有去重新获取数据库连接, 还是使用第一次的连接
 * 所以这里重写了事务管理器, 每次都会重新获取数据库连接, 并将连接缓存到 datasourceConnMap 中
 * 从而实现不同的数据源获取不同的连接, 从而开启不同的事务
 */
public class MultiDataSourceTransaction implements Transaction {

    private final DataSource dataSource;

    private final ConcurrentMap<String, Connection> datasourceConnMap;

    private boolean autoCommit;

    public MultiDataSourceTransaction(DataSource dataSource) {
        Assert.notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
        datasourceConnMap = MapUtil.newConcurrentHashMap();
    }

    /**
     * 获取数据库连接
     * 每次都根据数据源标识获取数据库连接, 并将连接缓存到 datasourceConnMap 中
     * 从而实现不同的数据源获取不同的连接, 从而开启不同的事务
     * spring 原生的只会获取一次连接, 所以会导致开启事务时切换数据源失效
     */
    @Override
    public Connection getConnection() throws SQLException {
        String ds = DynamicDataSourceContextHolder.peek();
        if (StrUtil.isBlank(ds)) {
            ds = DataSourceType.MASTER;
        }

        if (this.datasourceConnMap.containsKey(ds)) {
            return this.datasourceConnMap.get(ds);
        }

        Connection conn = this.dataSource.getConnection();
        autoCommit = false;
        conn.setAutoCommit(false);
        this.datasourceConnMap.put(ds, conn);
        return conn;
    }

    /**
     * 提交事务
     * 将所有的数据源连接分别进行事务的提交
     */
    @Override
    public void commit() throws SQLException {
        for (Connection conn : this.datasourceConnMap.values()) {
            if (!autoCommit) {
                conn.commit();
            }
        }
    }

    /**
     * 回滚事务
     * 将所有的数据源连接分别进行事务的回滚
     */
    @Override
    public void rollback() throws SQLException {
        for (Connection conn : this.datasourceConnMap.values()) {
            conn.rollback();
        }
    }

    /**
     * 关闭连接
     * 将所有的数据源连接分别进行关闭
     */
    @Override
    public void close() {
        for (Connection conn : this.datasourceConnMap.values()) {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    @Override
    public Integer getTimeout() {
        return null;
    }

}
