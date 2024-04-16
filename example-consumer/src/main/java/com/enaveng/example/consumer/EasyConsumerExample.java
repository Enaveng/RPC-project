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
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("tom");
        //调用接口
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(user.getName());
        } else {
            System.out.println("user is null");
        }
    }
}
