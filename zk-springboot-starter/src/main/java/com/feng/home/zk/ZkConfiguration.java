package com.feng.home.zk;

import com.feng.home.zk.enums.RetryPolicyEnum;
import lombok.Data;

/**
 * create by FengZiyu
 * 2020/01/10
 */
@Data
public class ZkConfiguration {
    //zk地址
    private String zkHost;

    //会话超时时间
    private int sessionTimeoutMs = 60000;
    //链接超时时间
    private int connectionTimeoutMs = 15000;

    //重试间隔时间
    private int baseSleepTimeMs = 1000;
    //重试次数
    private int maxRetries = 3;
    //最大重试间隔
    private int maxSleepMs = Integer.MAX_VALUE;
    //最大重试时间
    private int maxRetryTimeMs = Integer.MAX_VALUE;

    //重试策略
    private RetryPolicyEnum retryPolicy;

    //命名空间
    private String nameSpace;
}
