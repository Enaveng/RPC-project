package com.enaveng.example.consumer;

import com.enaveng.example.common.model.User;
import com.enaveng.example.common.service.UserService;
import com.enaveng.rpc.proxy.ServiceProxy;
import com.enaveng.rpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //参数为 指定代理类所需要实现的接口
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("tom");
        //调用接口
        User newUser = userService.getUser(user); //使用代理对象调用方法 实际是将方法的调用转发到ServiceProxy的invoke方法当中 通过远程调用的方法得到user对象
        if (newUser != null) {
            System.out.println(user.getName());
        } else {
            System.out.println("user is null");
        }
    }
}
