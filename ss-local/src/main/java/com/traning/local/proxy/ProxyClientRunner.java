package com.traning.local.proxy;

import com.traning.local.echo.ClientRequestInitHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端配置
 *
 * @author Wang Junwei
 * @date 2021/7/21
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class ProxyClientRunner {

    String host;

    int port;

    public ProxyClientRunner(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        String host = System.getProperty("host", "127.0.0.1");
        int port = Integer.parseInt(System.getProperty("port", "7777"));
        ProxyClientRunner client = new ProxyClientRunner(host, port);
        client.run();
    }


    public void run() throws Exception {
        // 用来接收进来的连接
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 启动 NIO 服务的辅助启动类，与服务端有所区别
            Bootstrap b = new Bootstrap();
            // 即作为一个 boss group ，也会作为一个 worker group，尽管客户端不需要使用到 boss worker
            b.group(worker)
                    // 新的Channel 如何接收进来的连接
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ClientRequestInitHandler())
            ;
            ChannelFuture channelFuture = b.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭事件循环器
            worker.shutdownGracefully();
        }
    }


}