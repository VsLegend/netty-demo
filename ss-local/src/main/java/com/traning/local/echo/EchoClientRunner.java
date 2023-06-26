package com.traning.local.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户端配置
 *
 * @author Wang Junwei
 * @date 2021/7/21
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class EchoClientRunner {

    private String host;
    private Integer port;

    public EchoClientRunner(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        EchoClientRunner client = new EchoClientRunner("127.0.0.1", 8001);
        client.start();
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
            // 创建连接后手动发送一个请求
            f.channel().writeAndFlush("Hello!");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[]{
                new StringDecoder(),
                new ClientStringHandler(),
                new StringEncoder()
        };
    }
}
