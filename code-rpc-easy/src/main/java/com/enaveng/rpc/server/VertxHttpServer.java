package com.enaveng.rpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
//        server.requestHandler(request->{
//            //处理Http请求
//            System.out.println("Received request: "+ request.method()+""+request.uri());
//
//            //发送Http响应
//            request.response()
//                    .putHeader("content-type","text/plain")
//                    .end("Hello from Vert.x HTTP server!");
//        });
        server.requestHandler(new HttpServerHandler());


        //启动HTTP服务器并监听指定的端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listen port " + port);
            } else {
                System.out.println("Failed to start server " + result.cause());
            }
        });

    }
}
