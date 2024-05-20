package com.enaveng.rpc.ProviderBootstrap;

import com.enaveng.rpc.RpcApplication;

/**
 * 服务消费者启动类
 */
public class ConsumerBootstrap {
    public static void init() {
        /**
         * 初始化(配置和注册中心)
         */
        RpcApplication.init();
    }

}
