package com.feng.home.common.jdbc.config;

import com.feng.home.common.jdbc.base.DaoMapping;
import com.feng.home.common.bean.ReflectUtils;
import com.feng.home.common.scanner.PackageScanner;
import com.feng.home.common.scanner.SamplePackageScanner;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(DataSourceConfig.class)
public class JdbcStarterAutoConfiguration {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Bean
    @ConditionalOnMissingBean(JdbcTemplate.class)
    JdbcTemplate jdbcSupport() throws Exception {
        return new JdbcTemplate(shardingDataSource());
    }

    @Bean
    DataSource shardingDataSource() throws Exception {
        List<DaoMapping> modelMappings = getMappings(dataSourceConfig.getBasePackage());
        //创建分库分表规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        modelMappings.stream().filter(DaoMapping::sharding).collect(Collectors.toList()).forEach(modelMapping -> {
            TableRuleConfiguration tableRule = new TableRuleConfiguration();
            //获取可映射的真实数据节点
            String actDataSources = modelMapping.actualDataSource().equals("default") ? dataSourceConfig.getDefaultDataSource() : modelMapping.actualDataSource();
            String acTables = modelMapping.actualTable().equals("default") ? modelMapping.logicTable() : modelMapping.actualTable();
            String realDataNode = actDataSources + "." + acTables;
            tableRule.setActualDataNodes(realDataNode);
            tableRule.setLogicTable(modelMapping.logicTable());
            //设置分表算法
            StandardShardingStrategyConfiguration tableConfiguration = new StandardShardingStrategyConfiguration(modelMapping.shardingColumn(),
                    ReflectUtils.getSampleInstance(modelMapping.tablePreciseShardingAlgorithm()),
                    ReflectUtils.getSampleInstance(modelMapping.tableRangeShardingAlgorithm()));
            tableRule.setTableShardingStrategyConfig(tableConfiguration);
            //设置分库算法
            StandardShardingStrategyConfiguration dbConfiguration = new StandardShardingStrategyConfiguration(modelMapping.shardingColumn(),
                    ReflectUtils.getSampleInstance(modelMapping.dbPreciseShardingAlgorithm()),
                    ReflectUtils.getSampleInstance(modelMapping.dbRangeShardingAlgorithm()));
            tableRule.setDatabaseShardingStrategyConfig(dbConfiguration);

            shardingRuleConfiguration.getTableRuleConfigs().add(tableRule);
        });
        shardingRuleConfiguration.setDefaultDataSourceName(dataSourceConfig.getDefaultDataSource());
        return ShardingDataSourceFactory.createDataSource(getDataSources(dataSourceConfig.getDataSourceList()),
                shardingRuleConfiguration, new ConcurrentHashMap<>(), new Properties());
    }

    private Map<String, DataSource> getDataSources(List<Map<String, String>> dataSourceMetaList){
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMetaList.forEach(meta -> {
            DataSource dataSource = DataSourceBuilder.create()
                    .driverClassName(meta.get("driverClass"))
                    .url(meta.get("url"))
                    .username(meta.get("username"))
                    .password(meta.get("password"))
                    .build();
            dataSourceMap.put(meta.get("name"), dataSource);
        });
        return dataSourceMap;
    }

    private List<DaoMapping> getMappings(String basePackage) throws Exception {
        PackageScanner scanner = new SamplePackageScanner();
        try {
            List<DaoMapping> modelMappings = scanner.scanAndGetAnnotations(DaoMapping.class, basePackage);
            return modelMappings;
        } catch (Exception e) {
            throw new Exception("modelMapping 映射获取失败:",e);
        }
    }

}
