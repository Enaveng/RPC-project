package com.enaveng.rpc;

import com.enaveng.rpc.serializable.Serializer;

import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) {
        //利用Java自带的ServiceLoader动态加载指定接口的实现类 获取到文件中编写的所有实现类对象
        Serializer serializer = null;
        ServiceLoader<Serializer> serializers = ServiceLoader.load(Serializer.class);
        for (Serializer service : serializers) {
            serializer = service;
        }
    }

}
