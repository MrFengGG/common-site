package com.feng.home.common.jdbc.pagination;

import java.util.HashSet;
import java.util.Set;

public class PaginationSupportFactory {
    private static final Set<PaginationSupport> paginationSupports = new HashSet<>();

    static {
        addPaginationSupport(new MysqlPaginationSupport());
    }

    public static void addPaginationSupport(PaginationSupport paginationSupport){
        paginationSupports.add(paginationSupport);
    }

    public static PaginationSupport getSuitableSupport(String dbType){
        return paginationSupports.stream()
                .filter(paginationSupport -> paginationSupport.support(dbType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("不支持的数据库类型:" + dbType));
    }
}
