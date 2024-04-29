package com.enaveng.rpc.model;

import cn.hutool.core.util.StrUtil;
import com.enaveng.rpc.constant.RpcConstant;
import lombok.Data;

/**
 * 服务元信息(注册信息)
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;


    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 服务分组(未实现)
     */
    private String serviceGroup = "default";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;


    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s:%s", getServiceKey(), serviceAddress);
    }


    /**
     * 获取完整服务地址
     */
    public String getServiceAddress(String serviceHost, Integer servicePort) {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
