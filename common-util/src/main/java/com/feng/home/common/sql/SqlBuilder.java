package com.feng.home.common.sql;

import com.feng.home.common.common.StringUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public final class SqlBuilder {
    private StringBuilder sqlBuilder;

    private LinkedList<Object> params;

    private SqlBuilder(String initSql) {
        this.sqlBuilder = new StringBuilder(initSql);
        this.params = new LinkedList<>();
    }

    public static SqlBuilder init(String str){
        return new SqlBuilder(str);
    }

    public String getSql(){
        return sqlBuilder.toString();
    }

    public Object[] getParamArray(){
        return params.toArray();
    }

    public SqlBuilder joinDirect(String sql, Object param){
        sqlBuilder.append(" ").append(sql);
        params.add(param);
        return this;
    }

    public SqlBuilder joinDirect(String sql){
        sqlBuilder.append(" ").append(sql);
        return this;
    }

    public SqlBuilder join(String sql, Object param){
        if(param != null){
            sqlBuilder.append(" ").append(sql);
            params.add(param);
        }
        return this;
    }

    public SqlBuilder joinLikeBefore(String sql, Object param){
        if(param != null){
            sqlBuilder.append(" ").append(sql).append(" like? ");
            params.add("%" + param);
        }
        return this;
    }

    public SqlBuilder joinLikeAfter(String sql, Object param){
        if(param != null){
            sqlBuilder.append(" ").append(sql).append(" like? ");
            params.add(param + "%");
        }
        return this;
    }
    public SqlBuilder joinLikeAround(String sql, Object param){
        if(param != null){
            sqlBuilder.append(" ").append(sql).append(" like? ");
            params.add("%" + param + "%");
        }
        return this;
    }

    public SqlBuilder joinIn(String sql, Object[] paramArray){
        if(paramArray.length > 0){
            sqlBuilder.append(" ").append(sql).append(" in(").append(StringUtil.getEmptyParams(paramArray.length)).append(")");
            params.addAll(Arrays.stream(paramArray).collect(Collectors.toList()));
        }
        return this;
    }
}
