package com.feng.home.common.jdbc.pagination;

public interface PaginationSupport {
    public <T> String getPaginationSql(String sql, Page<T> page);

    public String getCountSql(String sql);

    public boolean support(String dbType);
}
