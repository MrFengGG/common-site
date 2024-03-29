package com.feng.home.common.jdbc.pagination;


import com.feng.home.common.common.StringUtil;

public class MysqlPaginationSupport implements PaginationSupport {
    @Override
    public <T> String getPaginationSql(String sql, Page<T> page) {
        long startIndex = (page.getPageNo() - 1) * page.getPageSize();
        long endIndex = startIndex + page.getPageSize();
        StringBuilder sqlBuilder = new StringBuilder(sql);
        if(!StringUtil.isEmpty(page.getSortBy())){
            sqlBuilder.append(" ORDER BY ").append(page.getSortBy()).append(page.getRank());
        }
        sqlBuilder.append(" limit ").append(startIndex).append(",").append(endIndex);
        return sqlBuilder.toString().toLowerCase();
    }

    @Override
    public String getCountSql(String sql) {
        return ("SELECT COUNT(*) FROM (" + sql + ") A").toLowerCase();
    }

    @Override
    public boolean support(String dbType) {
        return dbType.equalsIgnoreCase("MYSQL");
    }
}
