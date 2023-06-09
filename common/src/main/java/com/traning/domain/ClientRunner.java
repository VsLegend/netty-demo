package com.traning.domain;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PreDestroy;

/**
 * 客户端服务
 *
 * @author Wang Junwei
 * @date 2023/6/9 10:39
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public abstract class ClientRunner extends Runner {

    @Override
    public void start() throws Exception {
        startCheck();
        ServerBootstrap b = new ServerBootstrap();
        channelHandlers = createChannelHandlers();
        b.group(worker)
                // 新的Channel 如何接收进来的连接
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(channelHandlers);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128);
        // 绑定监听服务端口，并开始接收进来的连接
        ChannelFuture channelFuture = b.bind(host, port).sync();
        if (!channelFuture.isSuccess()) {
            channelFuture.cause().printStackTrace();
        }
    }

    @Override
    @PreDestroy
    public void stop() throws Exception {
        worker.shutdownGracefully();
    }

}
