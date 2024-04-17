package com.enaveng.rpc.config;


import lombok.Data;

/**
 * Rpc框架配置
 */
@Data
public class RpcConfig {

    private RpcConfig(){

    }

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
}
