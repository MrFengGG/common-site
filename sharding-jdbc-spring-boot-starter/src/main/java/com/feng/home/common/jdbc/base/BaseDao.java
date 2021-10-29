package com.feng.home.common.jdbc.base;

import com.feng.home.common.exception.AssertUtil;
import com.feng.home.common.jdbc.pagination.Page;
import com.feng.home.common.jdbc.pagination.PaginationSupport;
import com.feng.home.common.jdbc.pagination.PaginationSupportFactory;
import com.feng.home.common.bean.BeanUtils;
import com.feng.home.common.common.StringUtil;
import com.feng.home.common.jdbc.utils.SqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.*;

@Validated
public abstract class BaseDao{

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String dbType;

    /**
     * 分页查询
     * @param page
     * @param modelClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> Page<T> queryForPaginationBean(@NotNull Page<T> page, @NotNull Class<T> modelClass, @NotBlank String sql, @NotNull Object[] args){
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

    /**
     * 分页查询
     * @param page
     * @param modelClass
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T> Page<T> queryForPaginationBean(@NotNull Page<T> page, @NotNull Class<T> modelClass, @NotNull SqlBuilder sqlBuilder){
        return queryForPaginationBean(page, modelClass, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }

    /**
     * 快速查询全部
     * @param modelClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> queryForAllBean(@NotNull Class<T> modelClass, @NotBlank String sql, @NotNull Object[] args){
        return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(modelClass), args);
    }

    /**
     * 快速查询全部
     * @param modelClass
     * @param sqlBuilder
     * @param <T>
     * @return
     */
    public <T> List<T> queryForAllBean(@NotNull Class<T> modelClass, @NotNull SqlBuilder sqlBuilder){
        return this.queryForAllBean(modelClass, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }

    /**
     * 快速查询首个Bean
     * @param modelClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> Optional<T> findFirstBean(@NotNull Class<T> modelClass, @NotBlank String sql, @NotNull Object[] args){
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

    /**
     * 快速分页查询Map
     * @param page
     * @param sql
     * @param args
     * @return
     */
    public Page<Map<String, Object>> queryForPaginationMap(@NotNull Page<Map<String, Object>> page, @NotBlank String sql, @NotNull Object[] args) {
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

    /**
     * 根据SqlBuilder快速分页查询接口
     * @param page
     * @param sqlBuilder
     * @return
     */
    public Page<Map<String, Object>> queryForPaginationMap(@NotNull Page<Map<String, Object>> page, @NotNull SqlBuilder sqlBuilder){
        return queryForPaginationMap(page, sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }

    /**
     * 根据SqlBuilder快速更新
     * @param sqlBuilder
     * @return
     */
    public int update(@NotNull SqlBuilder sqlBuilder){
        return this.update(sqlBuilder.getSql(), sqlBuilder.getParamArray());
    }

    /**
     * 根据SQL进行更新
     * @param sql
     * @param args
     * @return
     */
    public int update(@NotBlank String sql, @NotNull Object[] args){
        return jdbcTemplate.update(sql, args);
    }

    /**
     * 快速更新javabean对象
     * @param column 数据库字段名称
     * @param bean
     * @param table
     * @param <T>
     * @return
     */
    public <T> int updateBean(@NotBlank String column, @NotNull T bean, @NotBlank String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMapWithUnderScore(bean);
        Object selectValue = parameterMap.remove(column);

        List<Object> params = new ArrayList<>(parameterMap.values());
        params.add(selectValue);
        String sql = getUpdateSql(parameterMap, table, column);
        return this.jdbcTemplate.update(sql, params.toArray());
    }

    /**
     * 快速根据ID进行修改
     * @param bean
     * @param table
     * @param <T>
     * @return
     */
    public <T> int updateById(@NotNull T bean, String table){
        return updateBean("id", bean, table);
    }

    /**
     * 快速根据字段删除
     * @param column
     * @param value
     * @param table
     * @return
     */
    public int remove(@NotBlank String column, @NotNull Object value, @NotBlank String table){
        String sql = "delete from " + table + " where " + column + " =?";
        return this.jdbcTemplate.update(sql, value);
    }

    /**
     * 快速根据字段批量删除
     * @param column
     * @param valueArray
     * @param table
     * @return
     */
    public int remove(@NotBlank String column, @NotEmpty Object[] valueArray, @NotBlank String table){
        String sql = "delete from " + table + " where " + column + " in(" + StringUtil.getEmptyParams(valueArray.length) + ")";
        return this.jdbcTemplate.update(sql, valueArray);
    }

    /**
     * 快速根据ID删除
     * @param id
     * @param table
     * @return
     */
    public int removeById(@NotNull Object id, @NotBlank String table){
        return remove("id", id, table);
    }

    /**
     * 快速根据ID批量删除
     * @param array
     * @param table
     * @return
     */
    public int removeByIdArray(@NotEmpty Object[] array, @NotBlank String table){
        return remove("id", array, table);
    }
    /**
     * 保存单个对象
     * @param bean
     * @param table
     * @param <T>
     */
    public <T> void saveBean(T bean, String table){
        Map<String, Object> parameterMap = BeanUtils.transBeanToMapWithUnderScore(bean);
        String sql = this.getSaveSql(parameterMap, table);
        this.jdbcTemplate.update(sql, parameterMap.values().toArray());
    }

    /**
     * 批量保存javaBean对象,使用NoConvertField标注的字段不会保存
     * @param beans
     * @param table
     * @param <T>
     */
    public <T> void saveBeanList(List<T> beans, String table){
        if(beans.size() <= 0){
            return;
        }
        String sql = null;
        List<Object[]> batchArgs = new LinkedList<>();
        for(T bean : beans) {
            Map<String, Object> beanPropertyMap = BeanUtils.transBeanToMapWithUnderScore(bean);
            if (sql == null) {
                sql = getSaveSql(beanPropertyMap, table);
            }
            batchArgs.add(beanPropertyMap.values().toArray());
        }
        this.jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 快速根据主健ID(id)进行单条记录查询
     * @param id
     * @param modelClass
     * @param table
     * @param <T>
     * @return
     */
    public <T> Optional<T> findById(Integer id, Class<T> modelClass, String table){
        return findBy("id", modelClass, table, id);
    }

    /**
     * 根据数据库表指定的一个字段进行单条记录查询
     * @param key
     * @param modelClass
     * @param table
     * @param value
     * @param <T>
     * @return
     */
    public <T> Optional<T> findBy(String key, Class<T> modelClass, String table, Object value){
        T t;
        try {
            t = this.jdbcTemplate.queryForObject("select * from " + table + " where " + key + "=?", BeanPropertyRowMapper.newInstance(modelClass), value);
        }catch (EmptyResultDataAccessException e){
            t = null;
        }
        return Optional.ofNullable(t);
    }

    /**
     * 根据占位符sql和入参进行计数
     * @param sql
     * @param args
     * @return
     */
    public Integer count(String sql, Object[] args){
        return this.jdbcTemplate.queryForObject(sql, Integer.class, args);
    }

    /**
     * 根据SQLBuilder进行计数
     * @param sqlBuilder
     * @return
     */
    public Integer count(SqlBuilder sqlBuilder){
        AssertUtil.assertNotNull(sqlBuilder, "count sqlBuilder cannot be null");
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), Integer.class, sqlBuilder.getParamArray());
    }

    /**
     * 简单执行SQL并计数
     * @param sql
     * @return
     */
    public Integer count(String sql){
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 获取当前适用的分页插件
     * @return
     * @throws SQLException
     */
    private PaginationSupport getSuitablePaginationSupport() throws SQLException {
        try {
            return PaginationSupportFactory.getSuitableSupport(this.getCurrentDbType());
        } catch (SQLException e) {
            throw new SQLException("数据库分页查询失败,无法获取当前数据库类型:" + jdbcTemplate.getDataSource(), e);
        }
    }

    /**
     * 获取当前数据库类型
     * @return
     * @throws SQLException
     */
    private String getCurrentDbType() throws SQLException {
        if(StringUtil.isNotEmpty(dbType)){
            return dbType;
        }
        synchronized (this){
            dbType = this.jdbcTemplate.execute((ConnectionCallback<String>) connection ->
                    connection.getMetaData().getDatabaseProductName());
        }
        return dbType;
    }

    /**
     * 获取更新bean的SQL
     * @param beanPropertyMap
     * @param table
     * @param column
     * @return
     */
    private String getUpdateSql(Map<String, Object> beanPropertyMap, String table, String column){
        AssertUtil.assertTrue(beanPropertyMap.containsKey(column), String.format("column:%s not exist in bean", column));
        StringBuilder updateSqlBuilder = new StringBuilder("UPDATE ").append(table);
        boolean isFirstParam = true;
        for(String key : beanPropertyMap.keySet()){
            if(key.equals(column)){
                continue;
            }
            if (isFirstParam) {
                updateSqlBuilder.append(" SET ");
                isFirstParam = false;
            } else {
                updateSqlBuilder.append(",");
            }
            updateSqlBuilder.append(key).append("=").append("?");
        }
        updateSqlBuilder.append(" WHERE ").append(column).append("=?");
        return updateSqlBuilder.toString();
    }

    /**
     * 生成保存bean的SQL
     * @param beanPropertyMap
     * @param table
     * @return
     */
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
