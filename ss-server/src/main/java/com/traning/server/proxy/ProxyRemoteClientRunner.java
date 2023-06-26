package com.traning.server.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 代理服务器的远程请求客户端
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:13
 */
public class ProxyRemoteClientRunner {

    private String host;
    private Integer port;

    public ProxyRemoteClientRunner(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        // http：80    https：443
        ProxyRemoteClientRunner runner = new ProxyRemoteClientRunner("www.baidu.com", 80);
        runner.start();
    }


    public void start() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    // 新的Channel 如何接收进来的连接
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 设置出入消息的处理链
                            ch.pipeline().addLast(createChannel(ch));
                        }
                    });
            // 创建一个连接
            ChannelFuture f = b.connect(host, port).sync();
            //
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[]{
                new LoggingHandler(LogLevel.DEBUG),
                new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        System.out.println("接收远程服务器的消息：" + msg);
                    }
                }
        };
    }
}
