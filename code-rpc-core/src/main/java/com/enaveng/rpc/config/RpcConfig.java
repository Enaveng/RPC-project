package com.enaveng.rpc.config;


import cn.hutool.core.util.StrUtil;
import com.enaveng.rpc.fault.retry.RetryStrategyKeys;
import com.enaveng.rpc.fault.tolerant.TolerantStrategyKeys;
import com.enaveng.rpc.loadbalancer.LoadBalancer;
import com.enaveng.rpc.loadbalancer.LoadBalancerKeys;
import com.enaveng.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.enaveng.rpc.serializable.SerializerKeys;
import lombok.Data;

/**
 * Rpc框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "code-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private int serverPort = 8888;

    /**
     * 是否开启模拟调用
     */
    private boolean mock = false;

    /**
     * 使用的序列化器 默认为Jdk序列化
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡策略
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试机制策略
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
