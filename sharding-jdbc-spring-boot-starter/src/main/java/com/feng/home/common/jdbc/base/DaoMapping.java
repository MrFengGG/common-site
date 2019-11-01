package com.feng.home.common.jdbc.base;

import com.feng.home.common.jdbc.sharding.DefaultShardingAlgorithm;
import com.feng.home.common.jdbc.sharding.ModShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DaoMapping {
    //逻辑表
    String logicTable();
    //是否开启分库分表
    boolean sharding() default false;
    //用户分库分表的字段
    String shardingColumn() default "id";
    //可映射的表,若为default,映射到逻辑表
    String actualTable() default "default";
    //可映射的数据源,若为default,映射到主数据源
    String actualDataSource() default "default";
    //分表算法
    Class<? extends PreciseShardingAlgorithm> tablePreciseShardingAlgorithm() default ModShardingAlgorithm.class;
    //分表算法
    Class<? extends RangeShardingAlgorithm> tableRangeShardingAlgorithm() default ModShardingAlgorithm.class;
    //分库算法
    Class<? extends PreciseShardingAlgorithm> dbPreciseShardingAlgorithm() default DefaultShardingAlgorithm.class;
    //分库算法
    Class<? extends RangeShardingAlgorithm> dbRangeShardingAlgorithm() default DefaultShardingAlgorithm.class;
}
