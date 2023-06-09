package com.traning.runner;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PreDestroy;

/**
 * 服务端服务
 *
 * @author Wang Junwei
 * @date 2023/6/9 10:39
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public abstract class ServerRunner extends Runner {

    private final EventLoopGroup boss;

    public ServerRunner() {
        super();
        boss = new NioEventLoopGroup();
    }

    @Override
    public void start() throws Exception {
        startCheck();
        ServerBootstrap b = new ServerBootstrap();
        channelHandlers = createChannelHandlers();
        b.group(boss, worker)
                // 新的Channel 如何接收进来的连接
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        // 部分handler不是共享的
                        ch.pipeline().addLast(createChannelHandlers());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        // 绑定监听服务端口，并开始接收进来的连接
        ChannelFuture channelFuture = b.bind(port).sync();
        if (!channelFuture.isSuccess()) {
            channelFuture.cause().printStackTrace();
        }
    }

    @Override
    @PreDestroy
    public void stop() throws Exception {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
