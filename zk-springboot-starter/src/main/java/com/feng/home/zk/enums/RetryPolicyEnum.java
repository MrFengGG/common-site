package com.feng.home.zk.enums;

import com.feng.home.zk.ZkConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * create by FengZiyu
 * 2020/01/10
 */
public enum RetryPolicyEnum {

    //随着重试次数增加重试时间间隔变大,指数倍增长
    ExponentialBackoffRetryPolicy(){
        @Override
        public RetryPolicy retryPolicy(ZkConfiguration config) {
            return new ExponentialBackoffRetry(config.getBaseSleepTimeMs(), config.getMaxRetries(), config.getMaxSleepMs());
        }
    },
    //永远重试
    RetryForeverPolicy(){
        @Override
        public RetryPolicy retryPolicy(ZkConfiguration config) {
            return new RetryForever(config.getBaseSleepTimeMs());
        }
    },
    //重试maxRetries次
    RetryNTimesPolicy(){
        @Override
        public RetryPolicy retryPolicy(ZkConfiguration config) {
            return new RetryNTimes(config.getMaxRetries(), config.getBaseSleepTimeMs());
        }
    },
    //重试超过指定时间
    RetryUntilElapsedPolicy(){
        @Override
        public RetryPolicy retryPolicy(ZkConfiguration config) {
            return new RetryUntilElapsed(config.getMaxRetryTimeMs(), config.getBaseSleepTimeMs());
        }
    };


    public abstract RetryPolicy retryPolicy(ZkConfiguration config);
}
