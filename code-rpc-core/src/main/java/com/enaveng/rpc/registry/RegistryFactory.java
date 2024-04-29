package com.enaveng.rpc.registry;

import com.enaveng.rpc.utils.SpiLoader;

/**
 * 注册中心工厂
 */
public class RegistryFactory {
    static {
        //静态代码块首先执行
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 通过配置的注册中心类别获取不同的Registry实例对象
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
