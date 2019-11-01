package com.feng.home.common.jdbc.base;

import com.feng.home.common.jdbc.pagination.Page;
import com.feng.home.common.jdbc.pagination.PaginationSupport;
import com.feng.home.common.jdbc.pagination.PaginationSupportFactory;
import com.feng.home.common.bean.BeanUtils;
import com.feng.home.common.common.StringUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseDao{
    private JdbcTemplate jdbcTemplate;

    protected void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> Page<T> queryForPaginationBean(Page<T> page, Class<T> modelClass, String sql, Object[] args) throws SQLException{
        PaginationSupport paginationSupport = getSuitablePaginationSupport();
        int total = this.count(paginationSupport.getCountSql(sql));
        page.setTotal(total);
        List<T> data = this.jdbcTemplate.query(paginationSupport.getPaginationSql(sql, page), new BeanPropertyRowMapper<>(modelClass), args);
        page.setData(data);
        return page;
    }

    public <T> List<T> queryForAllBean(Class<T> modelClass, String sql, Object[] args){
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(modelClass), args);
    }

    public <T> Optional<T> findFirstBean(Class<T> modelClass, String sql, Object[] args){
        T t;
        try {
            t = this.jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(modelClass), args);
        }catch (EmptyResultDataAccessException e){
            t = null;
        }
        return Optional.ofNullable(t);
    }

    public Page<Map<String, Object>> queryForPaginationMap(Page<Map<String, Object>> page, String sql, Object[] args) throws SQLException{
        PaginationSupport paginationSupport = getSuitablePaginationSupport();
        int total = this.count(paginationSupport.getCountSql(sql));
        page.setTotal(total);
        List<Map<String, Object>> data = this.jdbcTemplate.queryForList(paginationSupport.getPaginationSql(sql, page), args);
        page.setData(data);
        return page;
    }

    public <T> int updateBean(String column, T bean, String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMap(bean);
        Object selectValue = parameterMap.remove(column);

        List<Object> params = new ArrayList<>(parameterMap.values());
        params.add(selectValue);
        String sql = getUpdateSql(parameterMap, table, column);
        return this.jdbcTemplate.update(sql, params.toArray());
    }

    public <T> int updateById(T bean, String table){
        return updateBean("id", bean, table);
    }

    public <T> void saveBean(T bean, String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMap(bean);
        String sql = this.getSaveSql(parameterMap, table);
        this.jdbcTemplate.update(sql, parameterMap.values().toArray());
    }

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

    public Integer count(String sql, Object[] args){
        return this.jdbcTemplate.queryForObject(sql, Integer.class, args);
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
