package com.traning.server.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理服务器的远程请求客户端
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:13
 */
@Slf4j
public class ProxyRemoteClientRunner {

    private final String host;
    private final Integer port;
    private final ChannelHandlerContext ctx;

    public ProxyRemoteClientRunner(String host, Integer port, ChannelHandlerContext ctx) {
        this.host = host;
        this.port = port;
        this.ctx = ctx;
    }

    public Channel connect() throws Exception {
        final Channel inbound = ctx.channel();
        Bootstrap b = new Bootstrap();
        // 与代理任务使用同一个eventLoop（处于同一线程），确保客户端和远程服务器的传输没有线程安全问题
        b.group(inbound.eventLoop()).channel(inbound.getClass())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_READ, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // 这里添加一个处理器，用于接收远程服务并将消息发送给客户端
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG),
                                new HttpClientCodec(),
                                new HttpContentCompressor((CompressionOptions[]) null),
                                new HttpServerExpectContinueHandler(),
                                new HttpObjectAggregator(65536),
                                new ProxyRemoteInboundHandler(inbound));
                    }
                });
        ChannelFuture f = b.connect(host, port);
        // 添加监听器，处理与远程服务器的连接状态事件
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("连接远程服务器成功，开始传输数据");
                // 与远程服务连接成功后，netty开始传输数据，这个时候代理服务器只有一个转发功能
                inbound.read();
            } else {
                log.debug("连接远程服务器失败，退出");
                // 关闭与客户端的连接
                inbound.close();
            }
        });
        return f.channel();
    }
}
