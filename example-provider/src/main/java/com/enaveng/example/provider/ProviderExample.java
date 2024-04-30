package com.enaveng.example.provider;

import com.enaveng.example.common.service.UserService;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RegistryConfig;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.model.ServiceMetaInfo;
import com.enaveng.rpc.registry.LocalRegistry;
import com.enaveng.rpc.registry.Registry;
import com.enaveng.rpc.registry.RegistryFactory;
import com.enaveng.rpc.server.VertxHttpServer;
import io.etcd.jetcd.Client;

public class ProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        Registry registry = RpcApplication.init();

        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName, UserServiceImpl.class);

        //注册服务到etcd注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
//        Registry registry = RegistryFactory.getInstance(registryConfig.getRegister());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //启动web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
