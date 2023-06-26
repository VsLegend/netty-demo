package com.traning.server.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 代理服务器
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:12
 */
public class ProxyServerRunner {

    private int port;

    private String proxyHost = "https://www.baidu.com/";

    public ProxyServerRunner(Integer port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        ProxyServerRunner proxyServerRunner = new ProxyServerRunner(8002);
        proxyServerRunner.start();
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 新的Channel 如何接收进来的连接
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 设置出入消息的处理链
                            ch.pipeline().addLast(createChannel(ch));
                        }
                    });
            // 绑定监听服务端口，并开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[]{
                new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        System.out.println("接收远程服务器的消息：" + msg);
                    }
                }
        };
    }
}
