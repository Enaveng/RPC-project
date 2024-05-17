package com.enaveng.rpc.fault.retry;

import com.enaveng.rpc.loadbalancer.LoadBalancer;
import com.enaveng.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.enaveng.rpc.utils.SpiLoader;

/**
 * 重试策略工厂
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    //默认的序列化器
    private static final RetryStrategy DEFAULT_LOAD_BALANCER = new FixedIntervalRetryStrategy();

    //获取序列化器
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
