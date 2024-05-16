package com.enaveng.rpc.loadbalancer;

import com.enaveng.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性 Hash环 存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        //构建虚拟换节点
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        //根据参数得到hash值
        int hash = getHash(requestParams);
        //从Hash环上得到相对应的服务地址
        //得到大于或等于给定hash值的最小键值对
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            //如果没有大于等于调用请求hash值的虚拟节点 则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * hash算法 可自行重写
     *
     * @param key
     * @return
     */
    private int getHash(Object key) {
        return key.hashCode();
    }

    public static void main(String[] args) {
        TreeMap<Integer, Object> map = new TreeMap<>();
        map.put(1, "A");
        map.put(2, "B");
//        map.put(3,"C");
        map.put(4, "D");
        map.put(5, "E");
        Map.Entry<Integer, Object> entry = map.ceilingEntry(3);
        System.out.println(entry.getKey());
        System.out.println(entry.getValue());
    }
}
