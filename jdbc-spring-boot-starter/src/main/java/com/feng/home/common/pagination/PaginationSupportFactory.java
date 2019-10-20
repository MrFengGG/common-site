package com.feng.home.common.pagination;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PaginationSupportFactory {
    private static final Set<PaginationSupport> paginationSupports = new HashSet<>();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    static {
        addPaginationSupport(new MysqlPaginationSupport());
    }

    public static void addPaginationSupport(PaginationSupport paginationSupport){
        readWriteLock.writeLock().lock();
        paginationSupports.add(paginationSupport);
        readWriteLock.writeLock().unlock();
    }

    public static PaginationSupport getSuitableSupport(String dbType){
        readWriteLock.readLock().lock();
        try {
            return paginationSupports.stream()
                    .filter(paginationSupport -> paginationSupport.support(dbType))
                    .findAny()
                    .orElseThrow(() -> {
                        return new IllegalArgumentException("不支持的数据库类型:" + dbType);
                    });
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
}
