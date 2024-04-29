package com.enaveng.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.enaveng.example.common.model.User;
import com.enaveng.example.common.service.UserService;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.config.RpcConfig;
import com.enaveng.rpc.model.RpcRequest;
import com.enaveng.rpc.model.RpcResponse;
import com.enaveng.rpc.serializable.JdkSerializer;
import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.serializable.SerializerFactory;

import java.io.IOException;

/**
 * 静态代理方式
 * 通过调用服务提供者接口的方式实现getUser方法
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        //指定序列化器
//        JdkSerializer jdkSerializer = new JdkSerializer();
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        //发请求
        RpcRequest rpcRequest = new RpcRequest.Builder()
                .setArgs(new Object[]{user})
                .setServiceName(UserService.class.getName())
                .setMethodName("getUser")
                .setParameterTypes(new Class[]{User.class})
                .build();
        try {
            //进行序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8011")
                    .body(bodyBytes)
                    .execute();
            result = httpResponse.bodyBytes(); //获取响应流字节码
            //将结果进行反序列化
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            //取出响应数据并转换为User对象
            return (User) rpcResponse.getData();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
