package com.enaveng.rpc.loadbalancer;

import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.utils.SpiLoader;

/**
 * 负载均衡器工厂(用于获取负载均衡器对象)
 */
public class LoadBalanceFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    //默认的负载均衡策略
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    //获取负载均衡器
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
