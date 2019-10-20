package com.feng.home.common.base;

import com.feng.home.common.pagination.Page;
import com.feng.home.common.pagination.PaginationSupport;
import com.feng.home.common.pagination.PaginationSupportFactory;
import com.feng.home.common.resource.JdbcSupport;
import com.feng.home.common.resource.ModelMapping;
import com.feng.home.common.utils.BeanMappingUtils;
import com.feng.home.common.common.StringUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseDao implements JdbcSupport {
    protected JdbcTemplate template;

    protected abstract void setTemplate(JdbcTemplate template);

    @Override
    public <T> Page<T> queryForPaginationBean(Page<T> page, Class<T> modelClass, String sql, Object[] args) throws SQLException {
        PaginationSupport paginationSupport = getSuitablePaginationSupport();
        int total = this.count(paginationSupport.getCountSql(sql));
        page.setTotal(total);
        List<T> data = template.query(paginationSupport.getPaginationSql(sql, page), new BeanPropertyRowMapper<>(modelClass), args);
        page.setData(data);
        return page;
    }

    @Override
    public <T> List<T> queryForAllBean(Class<T> modelClass, String sql, Object[] args) {
        return template.query(sql, new BeanPropertyRowMapper<>(modelClass), args);
    }

    @Override
    public <T> Optional<T> findFirstBean(Class<T> modelClass, String sql, Object[] args) {
        T t;
        try {
            t = template.queryForObject(sql, BeanPropertyRowMapper.newInstance(modelClass), args);
        }catch (EmptyResultDataAccessException e){
            t = null;
        }
        return Optional.ofNullable(t);
    }

    @Override
    public Page<Map<String, Object>> queryForPaginationMap(Page<Map<String, Object>> page, String sql, Object[] args) throws SQLException {
        PaginationSupport paginationSupport = getSuitablePaginationSupport();
        int total = this.count(paginationSupport.getCountSql(sql));
        page.setTotal(total);
        List<Map<String, Object>> data = this.template.queryForList(paginationSupport.getPaginationSql(sql, page), args);
        page.setData(data);
        return page;
    }

    @Override
    public <T> int updateBean(String column, T bean) {
        ModelMapping modelMapping = BeanMappingUtils.getModelMapping(bean);
        if(modelMapping == null){
            throw new IllegalArgumentException("unsupported bean to update:" + bean);
        }
        Map<String, Object> parameterMap = BeanMappingUtils.transBeanToQueryMap(bean);
        Object selectValue = parameterMap.remove(column);
        List<Object> params = new ArrayList<>(parameterMap.values());
        params.add(selectValue);
        String sql = getUpdateSql(parameterMap, modelMapping.logicTable(), column);
        return template.update(sql, params.toArray());
    }

    @Override
    public <T> void saveBean(T bean){
        ModelMapping modelMapping = BeanMappingUtils.getModelMapping(bean);
        if(modelMapping == null){
            throw new IllegalArgumentException("unsupported bean to save:" + bean + " with out @ModelMapping");
        }
        Map<String, Object> parameterMap = BeanMappingUtils.transBeanToQueryMap(bean);
        String sql = this.getSaveSql(parameterMap, modelMapping.logicTable());
        this.template.update(sql, parameterMap.values().toArray());
    }

    @Override
    public <T> void saveBeanList(List<T> beans){
        if(beans.size() <= 0){
            return;
        }
        String sql = null;
        ModelMapping modelMapping = null;
        List<Object[]> batchArgs = new LinkedList<>();
        for(T bean : beans){
            Map<String, Object> beanPropertyMap = BeanMappingUtils.transBeanToQueryMap(bean);
            if(modelMapping == null){
                modelMapping = BeanMappingUtils.getModelMapping(beans.get(0));
                if(modelMapping == null){
                    throw new IllegalArgumentException("unsupported bean to batchSave:" + beans);
                }
                sql = getSaveSql(beanPropertyMap, modelMapping.logicTable());
            }
            batchArgs.add(beanPropertyMap.values().toArray());
        }
        this.template.batchUpdate(sql, batchArgs);
    }

    @Override
    public int update(String sql, String[] args){
        return template.update(sql, args);
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs){
        return template.batchUpdate(sql, batchArgs);
    }

    @Override
    public int count(String sql, Object[] args) {
        return template.queryForObject(sql, Integer.class, args);
    }

    @Override
    public int count(String sql) {
        return template.queryForObject(sql, Integer.class);
    }

    private PaginationSupport getSuitablePaginationSupport() throws SQLException {
        try {
            return PaginationSupportFactory.getSuitableSupport(this.getCurrentDbType());
        } catch (SQLException e) {
            throw new SQLException("数据库分页查询失败,无法获取当前数据库类型:" + template.getDataSource(), e);
        }
    }

    private String getCurrentDbType() throws SQLException {
        return this.template.getDataSource().getConnection().getMetaData().getDatabaseProductName();
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
}
