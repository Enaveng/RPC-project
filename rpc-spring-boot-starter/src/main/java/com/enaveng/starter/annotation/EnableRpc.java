package com.enaveng.starter.annotation;

import com.enaveng.starter.bootstrap.RpcConsumerBootstrap;
import com.enaveng.starter.bootstrap.RpcInitBootstrap;
import com.enaveng.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动Rpc注解
 */
@Target({ElementType.TYPE})  //表示注解可以声明在哪些目标元素之前  ElementType.TYPE表示可以声明在类、注解上
@Retention(RetentionPolicy.RUNTIME) //表示这个注解将在运行时可通过反射获取到
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 是否需要启动Server
     * 方法不进行具体实现时默认返回true
     * @return
     */
    boolean needServer() default true;

}
