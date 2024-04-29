package com.enaveng.rpc.config;

import com.enaveng.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * 注册中心配置
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String register = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2379";

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 超时时间(单位毫秒)
     */
    private Long timeout = 10000L;


}
