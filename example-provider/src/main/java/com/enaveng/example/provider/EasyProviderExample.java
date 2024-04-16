package com.enaveng.example.provider;

import com.enaveng.example.common.service.UserService;
import com.enaveng.rpc.registry.LocalRegistry;
import com.enaveng.rpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //将服务注册到注册器当中
        LocalRegistry.registry(UserService.class.getName(),UserServiceImpl.class);

        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8888);
    }
}
