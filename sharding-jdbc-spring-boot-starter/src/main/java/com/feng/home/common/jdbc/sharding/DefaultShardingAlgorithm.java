package com.feng.home.common.jdbc.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.util.Collection;

/**
 * 返回第一个数据源或数据表的分库分表算法
 */
public class DefaultShardingAlgorithm implements RangeShardingAlgorithm<Integer>, PreciseShardingAlgorithm<Integer> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        return collection.stream().findFirst().orElseThrow(() -> new UnsupportedOperationException("没有数据源"));
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Integer> rangeShardingValue) {
        if(collection.size() > 1){
            throw new UnsupportedOperationException("DefaultDataSourceSharding只能有一个数据源");
        }
        return collection;
    }
}
