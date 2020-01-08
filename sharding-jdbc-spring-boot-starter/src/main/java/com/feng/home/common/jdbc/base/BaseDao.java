package com.feng.home.common.jdbc.base;

import com.feng.home.common.jdbc.pagination.Page;
import com.feng.home.common.jdbc.pagination.PaginationSupport;
import com.feng.home.common.jdbc.pagination.PaginationSupportFactory;
import com.feng.home.common.bean.BeanUtils;
import com.feng.home.common.common.StringUtil;
import com.feng.home.common.sql.SqlBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseDao{
    protected JdbcTemplate jdbcTemplate;

    protected void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //分页查询
    public <T> Page<T> queryForPaginationBean(Page<T> page, Class<T> modelClass, String sql, Object[] args){
        PaginationSupport paginationSupport = null;
        try {
            paginationSupport = getSuitablePaginationSupport();
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
        int total = this.count(paginationSupport.getCountSql(sql), args);
        page.setTotal(total);
        List<T> data = this.jdbcTemplate.query(paginationSupport.getPaginationSql(sql, page), new BeanPropertyRowMapper<>(modelClass), args);
        page.setData(data);
        return page;
    }
    public <T> Page<T> queryForPaginationBean(Page<T> page, Class<T> modelClass, SqlBuilder sqlBuilder){
        return queryForPaginationBean(page, modelClass, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }
    //全量查询
    public <T> List<T> queryForAllBean(Class<T> modelClass, String sql, Object[] args){
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(modelClass), args);
    }
    public <T> List<T> queryForAllBean(Class<T> modelClass, SqlBuilder sqlBuilder){
        return this.queryForAllBean(modelClass, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }
    //单项查询
    public <T> Optional<T> findFirstBean(Class<T> modelClass, String sql, Object[] args){
        T t;
        try {
            t = this.jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(modelClass), args);
        }catch (EmptyResultDataAccessException e){
            t = null;
        }
        return Optional.ofNullable(t);
    }
    public <T> Optional<T> findFirstBean(Class<T> modelClass, SqlBuilder sqlBuilder){
        return findFirstBean(modelClass, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }
    //分页map查询
    public Page<Map<String, Object>> queryForPaginationMap(Page<Map<String, Object>> page, String sql, Object[] args) {
        PaginationSupport paginationSupport = null;
        try {
            paginationSupport = getSuitablePaginationSupport();
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
        int total = this.count(paginationSupport.getCountSql(sql), args);
        page.setTotal(total);
        List<Map<String, Object>> data = this.jdbcTemplate.queryForList(paginationSupport.getPaginationSql(sql, page), args);
        page.setData(data);
        return page;
    }
    public Page<Map<String, Object>> queryForPaginationMap(Page<Map<String, Object>> page, SqlBuilder sqlBuilder){
        return queryForPaginationMap(page, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }
    //更新
    public int update(SqlBuilder sqlBuilder){
        return this.update(sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }

    public int update(String sql, Object[] args){
        return jdbcTemplate.update(sql, args);
    }
    //更新数据
    public <T> int updateBean(String column, T bean, String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMap(bean);
        Object selectValue = parameterMap.remove(column);

        List<Object> params = new ArrayList<>(parameterMap.values());
        params.add(selectValue);
        String sql = getUpdateSql(parameterMap, table, column);
        return this.jdbcTemplate.update(sql, params.toArray());
    }
    //根据ID更数据
    public <T> int updateById(T bean, String table){
        return updateBean("id", bean, table);
    }
    //删除
    public int remove(String column, Object value, String table){
        String sql = "delete from " + table + " where " + column + " =?";
        return this.jdbcTemplate.update(sql, value);
    }
    public int remove(String column, Object[] valueArray, String table){
        String sql = "delete from " + table + " where " + column + " in(" + StringUtil.getEmptyParams(valueArray.length) + ")";
        return this.jdbcTemplate.update(sql, valueArray);
    }
    public int removeById(Object id, String table){
        return remove("id", id, table);
    }
    public int removeByIdArray(Object[] array, String table){
        return remove("id", array, table);
    }
    //保存
    public <T> void saveBean(T bean, String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMap(bean);
        String sql = this.getSaveSql(parameterMap, table);
        this.jdbcTemplate.update(sql, parameterMap.values().toArray());
    }
    //批量保存
    public <T> void saveBeanList(List<T> beans, String table){
        if(beans.size() <= 0){
            return;
        }
        String sql = null;
        List<Object[]> batchArgs = new LinkedList<>();
        for(T bean : beans) {
            Map<String, Object> beanPropertyMap = BeanUtils.transBeanToMap(bean);
            if (sql == null) {
                sql = getSaveSql(beanPropertyMap, table);
            }
            batchArgs.add(beanPropertyMap.values().toArray());
        }
        this.jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    //根据ID查询
    public <T> Optional<T> findById(Integer id, Class<T> modelClass, String table){
        return findBy("id", modelClass, table, id);
    }
    //根据字段快速查询
    public <T> Optional<T> findBy(String key, Class<T> modelClass, String table, Object value){
        T t;
        try {
            t = this.jdbcTemplate.queryForObject("select * from " + table + " where " + key + "=?", BeanPropertyRowMapper.newInstance(modelClass), value);
        }catch (EmptyResultDataAccessException e){
            t = null;
        }
        return Optional.ofNullable(t);
    }
    //计数
    public Integer count(String sql, Object[] args){
        return this.jdbcTemplate.queryForObject(sql, Integer.class, args);
    }
    public Integer count(SqlBuilder sqlBuilder){
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), Integer.class, sqlBuilder.getParamArray());
    }
    public Integer count(String sql){
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private PaginationSupport getSuitablePaginationSupport() throws SQLException {
        try {
            return PaginationSupportFactory.getSuitableSupport(this.getCurrentDbType());
        } catch (SQLException e) {
            throw new SQLException("数据库分页查询失败,无法获取当前数据库类型:" + jdbcTemplate.getDataSource(), e);
        }
    }

    private String getCurrentDbType() throws SQLException {
        return this.jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
    }

    private String getUpdateSql(Map<String, Object> beanPropertyMap, String table, String column){
        StringBuilder updateSqlBuilder = new StringBuilder("UPDATE ").append(table);
        boolean isFirstParam = true;
        for(String key : beanPropertyMap.keySet()){
            if(!key.equals(column)){
                if (isFirstParam) {
                    updateSqlBuilder.append(" SET ");
                    isFirstParam = false;
                } else {
                    updateSqlBuilder.append(",");
                }
                updateSqlBuilder.append(key).append("=").append("?");
            }
        }
        updateSqlBuilder.append(" WHERE ").append(column).append("=?");
        return updateSqlBuilder.toString();
    }

    private String getSaveSql(Map<String, Object> beanPropertyMap, String table){
        return "INSERT INTO " +
                table +
                "(" +
                StringUtil.join(new ArrayList<>(beanPropertyMap.keySet()), ",") +
                ")" +
                "VALUES" +
                "(" +
                StringUtil.getEmptyParams(beanPropertyMap.size()) +
                ")";
    }
}
