package com.feng.home.common.sharding;

import com.google.common.collect.Range;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedList;

/**
 * 根据求模运算返回对应数据源和数据表的算法，key值必须为数字类型
 */
public class ModShardingAlgorithm implements RangeShardingAlgorithm<Comparable<Number>>, PreciseShardingAlgorithm<Comparable<Number>> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Comparable<Number>> rangeShardingValue) {
        Range<Comparable<Number>> valueRange = rangeShardingValue.getValueRange();
        Collection<String> resultCollection = new LinkedList<>();
        for (Long i = ((Number) valueRange.lowerEndpoint()).longValue(); i < ((Number) valueRange.upperEndpoint()).longValue(); i++) {
            for(String dataSource : collection){
                if(dataSource.endsWith(String.valueOf(i % collection.size()))){
                    resultCollection.add(dataSource);
                }
            }
        }
        return resultCollection;
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Comparable<Number>> preciseShardingValue) {
        Comparable a = preciseShardingValue.getValue();
        long value = ((Number) a).longValue();
        long size = collection.size();
        return collection.stream()
                .filter(dataSource -> { return dataSource.endsWith(String.valueOf(value % size)); })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("sharding failed because suitable target not found : " + preciseShardingValue));
    }
}
