package com.traning.server.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 事件处理器
 *
 * @author Wang Junwei
 * @date 2023/5/22 16:48
 *
 * Closed
 */
public class ServerStringHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("服务端接收到消息：" + msg);
//        String message = "HTTP/1.1 200 OK\n" +
//                "Content-Length: 35\n" +
//                "Date: " + new Date() + "\n" +
//                "Connection: keep-alive\n" +
//                "Content-Type: text/plain\n" +
//                "\n" +
//                "Reply, This is reply from server-.^";
//        ctx.write(msg);
//        ctx.flush();
        ctx.writeAndFlush(msg);
    }

}