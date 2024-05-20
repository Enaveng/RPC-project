package com.enaveng.provider;


import com.enaveng.example.common.model.User;
import com.enaveng.example.common.service.UserService;
import com.enaveng.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {


    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }

    @Override
    public int getNub() {
        return UserService.super.getNub();
    }
}
