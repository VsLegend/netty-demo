package com.traning.runner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.annotation.PreDestroy;

/**
 * 客户端服务
 *
 * @author Wang Junwei
 * @date 2023/6/9 10:39
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public abstract class ClientRunner extends Runner {

    /**
     * 用于主动发送数据
     */
    private ChannelFuture channelFuture;

    @Override
    public void start() throws Exception {
        startCheck();
        Bootstrap b = new Bootstrap();
        b.group(worker)
                // 新的Channel 如何接收进来的连接
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(createChannel(ch));
                    }
                });
        // 绑定监听服务端口，并开始接收进来的连接
        channelFuture = b.connect(host, port).sync();
        if (!channelFuture.isSuccess()) {
            channelFuture.cause().printStackTrace();
        }
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    @PreDestroy
    public void stop() throws Exception {
        worker.shutdownGracefully();
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }
}
