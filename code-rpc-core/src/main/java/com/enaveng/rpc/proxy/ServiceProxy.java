package com.enaveng.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.enaveng.rpc.RpcApplication;
import com.enaveng.rpc.model.RpcRequest;
import com.enaveng.rpc.model.RpcResponse;
import com.enaveng.rpc.serializable.JdkSerializer;
import com.enaveng.rpc.serializable.Serializer;
import com.enaveng.rpc.serializable.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * proxy:指代JDK动态生成的最终代理对象
     * method:指代的是我们所要调用真实对象的某个方法的Method对象
     * args:指代的是调用真实对象某个方法时接受的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取指定的序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
//        JdkSerializer jdkSerializer = new JdkSerializer();
        //构造请求对象
        RpcRequest rpcRequest = new RpcRequest.Builder()
                .setMethodName(method.getName())
                .setServiceName(method.getDeclaringClass().getName())
                .setArgs(args)
                .setParameterTypes(method.getParameterTypes())
                .build();
        //对象序列化调用接口
        byte[] bodyBytes = serializer.serialize(rpcRequest);
        //此处被硬编码 后续更改为从注册中心获取
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8011")
                .body(bodyBytes)
                .execute();
        byte[] result = httpResponse.bodyBytes(); //获取响应流字节码
        //将结果进行反序列化
        RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
        //取出响应数据并转换为User对象
        System.out.println(rpcResponse.getData());
        return rpcResponse.getData();
    }
}
