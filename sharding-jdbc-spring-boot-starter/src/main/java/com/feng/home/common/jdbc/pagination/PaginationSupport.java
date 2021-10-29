package com.feng.home.common.jdbc.pagination;

/**
 * 分页插件
 */
public interface PaginationSupport {
    /**
     * 用于将指定SQL加工为带分页的SQL
     * @param sql
     * @param page
     * @param <T>
     * @return
     */
    public <T> String getPaginationSql(String sql, Page<T> page);

    /**
     * 获取计数SQL
     * @param sql
     * @return
     */
    public String getCountSql(String sql);

    /**
     * 当前SQL插件是否支持指定数据库类型
     * @param dbType
     * @return
     */
    public boolean support(String dbType);
}
