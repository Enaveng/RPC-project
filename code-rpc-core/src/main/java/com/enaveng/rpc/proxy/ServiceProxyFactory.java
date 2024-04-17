package com.enaveng.rpc.proxy;


import com.enaveng.rpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 工厂模式 用于创建ServiceProxy代理类对象
 */
public class ServiceProxyFactory {

    /**
     * 返回代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        //判断是否开启模拟调用
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        //使用jdk自带的Proxy类来动态创建代理类对象
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }


    /**
     * 返回Mock代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
