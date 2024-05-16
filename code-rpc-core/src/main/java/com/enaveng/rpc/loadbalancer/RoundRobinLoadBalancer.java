package com.enaveng.rpc.loadbalancer;

import cn.hutool.core.collection.ListUtil;
import com.enaveng.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 当前轮询的下标
     * 使用原子操作类防止并发冲突问题 初始值为0
     */

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        //当注册中心当中只有一个服务地址时 表示不需要进行轮询
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        //否则使用取模轮询算法
        // currentIndex.getAndIncrement() 先返回当前值再将值加1
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);

    }

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (int i = 0; i < 10; i++) {
            System.out.println(atomicInteger.getAndIncrement());
        }
    }
}
