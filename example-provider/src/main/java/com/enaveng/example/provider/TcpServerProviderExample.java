package com.enaveng.example.provider;

import com.enaveng.example.common.service.UserService;
import com.enaveng.rpc.ProviderBootstrap.ProviderBootstrap;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RegistryConfig;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.model.ServiceMetaInfo;
import com.enaveng.rpc.model.ServiceRegisterInfo;
import com.enaveng.rpc.registry.LocalRegistry;
import com.enaveng.rpc.registry.Registry;
import com.enaveng.rpc.registry.RegistryFactory;
import com.enaveng.rpc.server.VertxHttpServer;
import com.enaveng.rpc.server.tcp.VertxTcpServer;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动使用TCP服务
 */
public class TcpServerProviderExample {
    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> list = new ArrayList<>();
        ServiceRegisterInfo<UserService> registerInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        list.add(registerInfo);
        ProviderBootstrap.init(list);
    }
}
