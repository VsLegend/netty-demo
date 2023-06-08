package com.traning.server.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端配置
 *
 * @author Wang Junwei
 * @date 2021/7/20
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class EchoServerRunner {

    int port;

    public EchoServerRunner(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        EchoServerRunner server = new EchoServerRunner(7777);
        server.run();
    }


    public void run() throws Exception {
        // NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器
        // 用来接收进来的连接
        EventLoopGroup boss = new NioEventLoopGroup();
        // 用来处理已经被接收的连接，一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 启动 NIO 服务的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    // 新的Channel 如何接收进来的连接
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerRequestInitHandler())
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
