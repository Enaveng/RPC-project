package com.enaveng.rpc.server;

import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.model.RpcRequest;
import com.enaveng.rpc.model.RpcResponse;
import com.enaveng.rpc.registry.LocalRegistry;
import com.enaveng.rpc.serializable.JdkSerializer;
import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.serializable.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Http请求处理
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器 -> 读取配置当中的序列化器类型 从对应的实体类对象中获取
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        //记录日志
        System.out.println("Received request: " + request.method() + "" + request.uri());

        /**
         * 不同服务器处理请求的方式是不同的 在这里使用的是vertx的处理方式
         */
        //异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            //反序列化请求为指定对象
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求对象为null 则直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpc request is null");
                //响应结果
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                //请求对象不为空时则进行调用
                Class<?> impClass = LocalRegistry.getService(rpcRequest.getServiceName());
                //获得指定方法名和参数类型的Method的对象
                Method method = impClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                //参数一表示要调用方法的对象实例 参数二表示传递的具体参数
                Object data = method.invoke(impClass.newInstance(), rpcRequest.getArgs());
                //封装
                rpcResponse.setData(data);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应结果
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应结果方法
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        //设置请求头
        HttpServerResponse httpServerResponse = request.response().putHeader("content-Type", "application/json");
        //序列化
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
