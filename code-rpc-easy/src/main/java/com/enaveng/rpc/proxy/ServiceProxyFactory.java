package com.enaveng.rpc.proxy;


import java.lang.reflect.Proxy;

/**
 * 工厂模式 用于创建ServiceProxy代理类对象
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        //使用jdk自带的Proxy类来动态创建代理类对象
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
