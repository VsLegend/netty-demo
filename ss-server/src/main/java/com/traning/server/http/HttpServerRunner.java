package com.traning.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * http请求
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:29
 */
public class HttpServerRunner {

    private int port;

    public HttpServerRunner(Integer port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        HttpServerRunner runner = new HttpServerRunner(8002);
        runner.start();
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LoggingHandler(LogLevel.DEBUG),
                                    // 集HttpRequestDecoder、HttpResponseEncoder为一体的解码编码器
                                    new HttpServerCodec(),
//                                    // 根据请求头的Accept-Encoding的压缩算法，对HttpMessage、HttpContent进行压缩
//                                    new HttpContentCompressor((CompressionOptions[]) null),
//                                    new HttpServerExpectContinueHandler(),
                                    new ServerHttpMessageHandler()
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
