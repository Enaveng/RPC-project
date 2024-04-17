package com.enaveng.rpc.model;

import java.io.Serializable;

/**
 * 封装返回信息
 */
public class RpcResponse implements Serializable {

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应数据类型
     */
    private Class<?> dataType;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 异常
     */
    private Exception exception;

    //练习建造者模式 可以使用@Build注解代替
    public RpcResponse(Builder builder) {
        this.data = builder.data;
        this.dataType = builder.dataType;
        this.message = builder.message;
        this.exception = builder.exception;
    }

    public RpcResponse() {
    }

    public static class Builder{
        private Object data;
        private Class<?> dataType;
        private String message;
        private Exception exception;

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public Builder setDataType(Class<?> dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setException(Exception exception) {
            this.exception = exception;
            return this;
        }

        public RpcResponse build(){
            return new RpcResponse(this);
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
