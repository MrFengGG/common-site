package com.feng.home.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("plus.jdbc")
public class DataSourceConfig {
    //默认数据源
    private String defaultDataSource;
    //包扫描路径
    private String basePackage = "";
    //数据源列表
    private List<Map<String, String>> dataSourceList = new LinkedList<>();

    private String enableSharding;

    public String getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public List<Map<String, String>> getDataSourceList() {
        return dataSourceList;
    }

    public void setDataSourceList(List<Map<String, String>> dataSourceList) {
        this.dataSourceList = dataSourceList;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getEnableSharding() {
        return enableSharding;
    }

    public void setEnableSharding(String enableSharding) {
        this.enableSharding = enableSharding;
    }
}
