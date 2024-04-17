package com.enaveng.rpc;

import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.constant.RpcConstant;
import com.enaveng.rpc.utils.ConfigUtils;

/**
 * RPC 框架应用
 * 实现的是初始化rpcConfig 双检锁单例模式
 */
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init() {
        RpcConfig newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        rpcConfig = newRpcConfig;
    }


    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
