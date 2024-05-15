package com.enaveng.rpc.model;

import java.io.Serializable;
import java.util.Arrays;

import com.enaveng.rpc.constant.RpcConstant;
import com.enaveng.rpc.serializable.Serializer;

/**
 * 封装调用所需的信息 是Java反射机制的必要参数
 */
public class RpcRequest implements Serializable {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 服务版本
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] args;

    public RpcRequest(Builder builder) {
        this.serviceName = builder.serviceName;
        this.methodName = builder.methodName;
        this.serviceVersion = builder.serviceVersion;
        this.parameterTypes = builder.parameterTypes;
        this.args = builder.args;
    }

    public RpcRequest() {
    }

    //建造者模式
    public static class Builder {
        private String serviceName;
        private String methodName;
        private String serviceVersion;
        private Class<?>[] parameterTypes;
        private Object[] args;

        public Builder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder setServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public RpcRequest build() {
            return new RpcRequest(this);
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
