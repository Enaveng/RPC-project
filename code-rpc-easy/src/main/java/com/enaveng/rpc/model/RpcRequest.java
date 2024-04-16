package com.enaveng.rpc.model;
import java.io.Serializable;
import java.util.Arrays;

import com.enaveng.rpc.serializable.Serializer;

/**
 * 封装调用所需的信息 是Java反射机制的必要参数
 */
public class RpcRequest implements Serializable{

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

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
        this.parameterTypes = builder.parameterTypes;
        this.args = builder.args;
    }

    public RpcRequest() {
    }

    public static class Builder {
        private String serviceName;
        private String methodName;
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

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public static void main(String[] args) {

    }
}
