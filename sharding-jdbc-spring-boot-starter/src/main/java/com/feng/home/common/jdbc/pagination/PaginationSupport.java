package com.feng.home.common.jdbc.pagination;

public interface PaginationSupport {
    public String getPaginationSql(String sql, Page page);

    public String getCountSql(String sql);

    public boolean support(String dbType);
}
