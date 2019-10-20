package com.feng.home.common.pagination;

public interface PaginationSupport {
    public String getPaginationSql(String sql, Page page);

    public String getCountSql(String sql);

    public boolean support(String dbType);
}
