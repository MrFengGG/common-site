package com.feng.home.common.pagination;


import com.feng.home.common.common.StringUtil;

public class MysqlPaginationSupport implements PaginationSupport {
    @Override
    public String getPaginationSql(String sql, Page page) {
        long startIndex = (page.getPageNo() - 1) * page.getPageSize();
        long endIndex = startIndex + page.getPageSize() - 1;
        StringBuilder sqlBuilder = new StringBuilder(sql);
        if(!StringUtil.isEmpty(page.getSortBy())){
            sqlBuilder.append(" ORDER BY ").append(page.getSortBy()).append(page.getRank());
        }
        sqlBuilder.append(" limit ").append(startIndex).append(",").append(endIndex);
        return sqlBuilder.toString().toUpperCase();
    }

    @Override
    public String getCountSql(String sql) {
        return ("SELECT COUNT(*) FROM (" + sql + ") A").toUpperCase();
    }

    @Override
    public boolean support(String dbType) {
        return dbType.equalsIgnoreCase("MYSQL");
    }
}
