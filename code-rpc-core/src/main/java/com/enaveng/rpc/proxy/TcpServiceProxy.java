package com.enaveng.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RegistryConfig;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.constant.RpcConstant;
import com.enaveng.rpc.fault.retry.RetryStrategy;
import com.enaveng.rpc.fault.retry.RetryStrategyFactory;
import com.enaveng.rpc.fault.tolerant.TolerantStrategy;
import com.enaveng.rpc.fault.tolerant.TolerantStrategyFactory;
import com.enaveng.rpc.loadbalancer.LoadBalanceFactory;
import com.enaveng.rpc.loadbalancer.LoadBalancer;
import com.enaveng.rpc.model.RpcRequest;
import com.enaveng.rpc.model.RpcResponse;
import com.enaveng.rpc.model.ServiceMetaInfo;
import com.enaveng.rpc.registry.Registry;
import com.enaveng.rpc.registry.RegistryFactory;
import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.serializable.SerializerFactory;
import com.enaveng.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 服务代理
 * 使用Tcp
 */
public class TcpServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        //构造请求对象
        RpcRequest rpcRequest = new RpcRequest.Builder()
                .setMethodName(method.getName())
                .setServiceName(serviceName)
                .setArgs(args)
                .setParameterTypes(method.getParameterTypes())
                .build();
        try {
            //将请求对象序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            //从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            //获取指定的Registry实例对象 此处对象为EtcdRegistry
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegister());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //添加负载均衡
            LoadBalancer loadBalancer = LoadBalanceFactory.getInstance(RpcApplication.getRpcConfig().getLoadBalancer());
            Map<String, Object> requestParam = new HashMap<>();
            String methodName = rpcRequest.getMethodName();
            requestParam.put("methodName", methodName);
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParam, serviceMetaInfoList);
            //添加重试机制
            RpcResponse rpcResponse = null;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(RpcApplication.getRpcConfig().getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() -> {
                    //调用Tcp服务
                    return VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
                });
            } catch (Exception e) {
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}