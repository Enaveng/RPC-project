package com.enaveng.rpc.ProviderBootstrap;

import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RegistryConfig;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.model.ServiceMetaInfo;
import com.enaveng.rpc.model.ServiceRegisterInfo;
import com.enaveng.rpc.registry.LocalRegistry;
import com.enaveng.rpc.registry.Registry;
import com.enaveng.rpc.registry.RegistryFactory;
import com.enaveng.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 启动使用TCP服务
 */
public class ProviderBootstrap {
    /**
     * 初始化
     *
     * @param serviceRegisterInfoList
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        //RPC框架初始化
        RpcApplication.init();

        //注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> implClass = serviceRegisterInfo.getImplClass();
            //本地注册
            LocalRegistry.registry(serviceName, implClass);
            //注册服务到etcd注册中心
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegister());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("注册服务失败" + e);
            }
        }
        //启动web服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
