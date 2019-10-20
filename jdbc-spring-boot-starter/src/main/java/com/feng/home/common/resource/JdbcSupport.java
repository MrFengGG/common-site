package com.feng.home.common.resource;


import com.feng.home.common.pagination.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JdbcSupport {
    <T> Page<T> queryForPaginationBean(Page<T> page, Class<T> modelClass, String sql, Object[] args) throws SQLException;

    <T> List<T> queryForAllBean(Class<T> modelClass, String sql, Object[] args);

    <T> Optional<T> findFirstBean(Class<T> modelClass, String sql, Object[] args);

    Page<Map<String, Object>> queryForPaginationMap(Page<Map<String, Object>> page, String sql, Object[] args) throws SQLException;

    <T> int updateBean(String column, T bean);

    <T> void saveBean(T bean);

    <T> void saveBeanList(List<T> beans);

    int update(String sql, String[] args);

    int[] batchUpdate(String sql, List<Object[]> batchArgs);

    int count(String sql, Object[] args);

    int count(String sql);
}
