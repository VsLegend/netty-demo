package com.traning.local.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 客户端配置
 *
 * @author Wang Junwei
 * @date 2023/7/17
 */
public class HttpClientRunner {

    private String host;
    private Integer port;

    public HttpClientRunner(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        HttpClientRunner client = new HttpClientRunner("127.0.0.1", 8002);
        client.start();
    }

    public void start() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG), new HttpClientCodec(), new ClientMessageToHttpHandler(), new ClientHttpReadHandler());
                }
            });
            // 创建一个连接
            ChannelFuture f = b.connect(host, port).sync();
            // 创建连接后手动发送一个请求
            f.channel().writeAndFlush("Hello!");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
