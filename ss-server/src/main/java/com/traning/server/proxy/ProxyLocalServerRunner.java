package com.traning.server.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 代理服务器
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:12
 */
public class ProxyLocalServerRunner {

    private final int port;

    private String remoteHost = "www.baidu.com";

    /**
     * http
     */
    public static final Integer REMOTE_PORT = 80;
    /**
     * https
     */
    public static final Integer REMOTE_HTTPS_PORT = 443;

    public ProxyLocalServerRunner(Integer port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        ProxyLocalServerRunner proxyLocalServerRunner = new ProxyLocalServerRunner(8002);
        proxyLocalServerRunner.start();
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(workerGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LoggingHandler(LogLevel.DEBUG),
                                    new HttpServerCodec(),
                                    new ProxyLocalHandler(remoteHost, REMOTE_PORT)
                            );
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
}
