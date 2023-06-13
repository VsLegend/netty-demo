package com.traning.local.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Wang Junwei
 * @date 2023/6/13 11:33
 */
public class ClientStringHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("客户端接收到消息：" + msg);
    }
}
