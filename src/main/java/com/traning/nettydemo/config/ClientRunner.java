package com.traning.nettydemo.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author Wong Jwei
 * @Date 2021/7/21
 * @Description 客户端
 */
public class ClientRunner {

    String host;

    int port;

    ChannelInboundHandler handler;

    public ClientRunner(int port) {
        this.port = port;
    }

    public ClientRunner(String host, int port, ChannelInboundHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;
    }


    public void run() throws Exception {
        EventLoopGroup worker = new NioEventLoopGroup(); // 用来接收进来的连接
        try {
            Bootstrap b = new Bootstrap(); // 启动 NIO 服务的辅助启动类，与服务端有所区别
            b.group(worker) // 即作为一个 boss group ，也会作为一个 worker group，尽管客户端不需要使用到 boss worker
                    .channel(NioSocketChannel.class) // 新的Channel 如何接收进来的连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(handler); // 注册服务处理器
                        }
                    })
            ;
            // 启动客户端
            ChannelFuture channelFuture = b.connect(host, port).sync();
            // 等待服务器socket关闭
            channelFuture.channel().closeFuture().sync();

        } finally {
            // 关闭事件循环器
            worker.shutdownGracefully();
        }
    }


}
