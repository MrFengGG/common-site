package com.feng.home.common.sql;

import com.feng.home.common.common.StringUtil;
import com.feng.home.common.validate.AssertUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SqlBuilder {
    private static final String UNSUPPORTED_ERROR = "[%s]语句不支持指定的操作";

    private boolean hasWhere = false;

    private List<Object> params = new LinkedList<>();

    private List<String> queryColumns;

    StringBuilder sqlStringBuilder;

    private SqlTypeEnum sqlType;

    private String table;

    private List<String> orderByList = new LinkedList<>();

    public SqlBuilder(SqlTypeEnum sqlType, String table){
        this.sqlType = sqlType;
        if(sqlType == SqlTypeEnum.SELECT){
            queryColumns = new LinkedList<>();
        }
        sqlStringBuilder = new StringBuilder();
        this.table = table;
    }

    public SqlBuilder selectFor(String ...column){
        AssertUtil.assertTrue(this.sqlType == SqlTypeEnum.SELECT, getUnsupportedError());
        this.queryColumns.addAll(Stream.of(column).collect(Collectors.toList()));
        return this;
    }

    public SqlBuilder whereEqual(String key, Object value){
        assertSupportWhere();
        beforeWhere();
        if(value == null){
            sqlStringBuilder.append(key).append(" is null ");
        }else{
            sqlStringBuilder.append(key).append(" = ? ");
            params.add(value);
        }
        return this;
    }

    public SqlBuilder whereEqualIfNotEmpty(String key, Object value){
        assertSupportWhere();
        if(!StringUtil.isEmpty(value)){
            return whereEqual(key, value);
        }
        return this;
    }

    public SqlBuilder whereNotEqual(String key, Object value){
        assertSupportWhere();
        beforeWhere();
        if(value == null){
            sqlStringBuilder.append(key).append(" is not null ");
        }else{
            sqlStringBuilder.append(key).append(" != ? ");
            params.add(value);
        }
        return this;
    }

    public SqlBuilder whereLike(String key, Object value){
        assertSupportWhere();
        if(value != null){
            beforeWhere();
            sqlStringBuilder.append(key).append(" like %? ");
            params.add(value);
        }
        return this;
    }

    public SqlBuilder whereIn(String key, Object ...value){
        assertSupportWhere();
        if(value.length > 0) {
            beforeWhere();
            sqlStringBuilder.append(key).append(" in ? ");
            params.add(StringUtil.join(value, ","));
        }
        return this;
    }

    public SqlBuilder whereIn(String key, Collection values){
        assertSupportWhere();
        if(values != null && values.size() <= 0) {
            beforeWhere();
            sqlStringBuilder.append(key).append(" in (?) ");
            params.add(StringUtil.join(values, ","));
        }
        return this;
    }

    public SqlBuilder whereGrater(String key, Object value){
        assertSupportWhere();
        beforeWhere();
        sqlStringBuilder.append(key).append(" > ? ");
        params.add(value);
        return this;
    }

    public SqlBuilder whereGraterIfNotEmpty(String key, Object value){
        assertSupportWhere();
        if(!StringUtil.isEmpty(value)){
            return whereGrater(key, value);
        }
        return this;
    }

    public SqlBuilder whereLess(String key, Object value){
        assertSupportWhere();
        beforeWhere();
        sqlStringBuilder.append(key).append(" < ? ");
        params.add(value);
        return this;
    }

    public SqlBuilder whereLessIfNotEmpty(String key, Object value){
        assertSupportWhere();
        if(!StringUtil.isEmpty(value)){
            return whereLess(key, value);
        }
        return this;
    }

    public SqlBuilder whereBetween(String key, Object start, Object end){
        assertSupportWhere();
        beforeWhere();
        sqlStringBuilder.append(key).append(" between ").append(start).append(" ? and ? ").append(end).append(" ");
        params.add(start);
        params.add(end);
        return this;
    }

    public SqlBuilder orderBy(String column, OrderTypeEnum orderTypeEnum){
        this.orderByList.add(column + " " + orderTypeEnum.value);
        return this;
    }

    public SqlResult build(){
        StringBuilder finalSqlBuilder = new StringBuilder(String.valueOf(this.sqlType));
        if(this.sqlType == SqlTypeEnum.SELECT){
            finalSqlBuilder.append(" ")
                    .append(queryColumns.size() <= 0 ? "*" : StringUtil.join(queryColumns, ","))
                    .append(" from ").append(table);
        }
        finalSqlBuilder.append(sqlStringBuilder);
        if(this.orderByList.size() > 0){
            finalSqlBuilder.append(" order by " + StringUtil.join(this.orderByList, ","));
        }
        return new SqlResult(finalSqlBuilder.toString(), params.toArray());
    }

    public static enum SqlTypeEnum{
        SELECT, UPDATE, ADD, REMOVE;
    }

    public static enum OrderTypeEnum{
        DESC("desc"), ASC("asc");

        OrderTypeEnum(String value) {
            this.value = value;
        }

        private String value;
    }

    public static final class SqlResult{
        public final String sql;

        public final Object[] param;

        public SqlResult(String sql, Object[] param) {
            this.sql = sql;
            this.param = param;
        }
    }

    private String getUnsupportedError(){
        return String.format(UNSUPPORTED_ERROR, sqlType);
    }

    private void assertSupportWhere(){
        AssertUtil.assertTrue(this.sqlType != SqlTypeEnum.ADD, getUnsupportedError());
    }

    private void beforeWhere(){
        if(!hasWhere){
            this.sqlStringBuilder.append(" where ");
            this.hasWhere = true;
        }else{
            this.sqlStringBuilder.append(" and ");
        }
    }
}
