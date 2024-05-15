package com.enaveng.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RegistryConfig;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.constant.RpcConstant;
import com.enaveng.rpc.model.RpcRequest;
import com.enaveng.rpc.model.RpcResponse;
import com.enaveng.rpc.model.ServiceMetaInfo;
import com.enaveng.rpc.protocol.*;
import com.enaveng.rpc.registry.Registry;
import com.enaveng.rpc.registry.RegistryFactory;
import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.serializable.SerializerFactory;
import com.enaveng.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
            //暂时先取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}