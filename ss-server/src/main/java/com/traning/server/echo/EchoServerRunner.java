package com.traning.server.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 服务端配置
 *
 * @author Wang Junwei
 * @date 2021/7/20
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class EchoServerRunner {

    private int port;

    public EchoServerRunner(Integer port) {
        this.port = port;
    }


    public static void main(String[] args) throws Exception {
        EchoServerRunner server = new EchoServerRunner(8001);
        server.start();
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
                new StringDecoder(), new StringEncoder(), new ServerStringHandler()
        };
    }
}
