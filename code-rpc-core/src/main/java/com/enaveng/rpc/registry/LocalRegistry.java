package com.enaveng.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册器
 * 本地注册器的主要作用是获取对应的类对象
 */
public class LocalRegistry {
    /**
     * 注册信息存储
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();


    /**
     * 注册服务
     * @param serviceName
     * @param implClass
     */
    public static void registry(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> getService(String serviceName){
        return map.get(serviceName);
    }


    /**
     * 删除服务
     * @param serviceName
     */
    public static void removeService(String serviceName){
        map.remove(serviceName);
    }



}
