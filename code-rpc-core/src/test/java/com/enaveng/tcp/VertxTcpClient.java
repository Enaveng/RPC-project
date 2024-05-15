package com.enaveng.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * 测试使用Tcp出现半包粘包问题
 */
public class VertxTcpClient {
    public void start() {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888, "localhost", result -> {
            if (result.succeeded()) {
                System.out.println("connect succeed");
                NetSocket socket = result.result();
                for (int i = 0; i < 1000; i++) {
                    //发送数据
                    socket.write("Hello,server!Hello,server!Hello,server!Hello,server!");
                }
                //接收响应
                socket.handler(buffer -> {
                    System.out.println("得到响应" + buffer.toString());
                });
            } else {
                System.out.println("failed to connect to tcp server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
