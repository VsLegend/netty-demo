package com.traning.nettydemo.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author Wong Jwei
 * @Date 2021/7/20
 * @Description 服务端
 */

public class ServerRunner {

    int port;

    ChannelInboundHandler handler;

    public ServerRunner(int port) {
        this.port = port;
    }

    public ServerRunner(int port, ChannelInboundHandler handler) {
        this.port = port;
        this.handler = handler;
    }


    public void run() throws Exception {
        // NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器
        EventLoopGroup boss = new NioEventLoopGroup(); // 用来接收进来的连接
        EventLoopGroup worker = new NioEventLoopGroup(); // 用来处理已经被接收的连接，一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
        try {
            ServerBootstrap b = new ServerBootstrap(); // 启动 NIO 服务的辅助启动类
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class) // 新的Channel 如何接收进来的连接
                    // 这里的事件处理类经常会被用来处理一个最近的已经接收的 Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 帮助使用者配置一个新的 Channel
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(handler); // 注册服务处理器
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定监听服务端口，并开始接收进来的连接
            ChannelFuture channelFuture = b.bind(port).sync();
            // 等待服务器socket关闭
            channelFuture.channel().closeFuture().sync();

        } finally {
            // 关闭事件循环器
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
