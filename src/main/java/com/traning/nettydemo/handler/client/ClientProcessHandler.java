package com.traning.nettydemo.handler.client;

import com.traning.nettydemo.handler.RequestData;
import com.traning.nettydemo.handler.ResponseData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 事件处理器
 *
 * @author Wang Junwei
 * @date 2023/5/22 16:48
 */
public class ClientProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在这里模拟一次客户端发送数据
        RequestData data = new RequestData();
        data.setId(999);
        data.setName("荷花");
        ChannelFuture channelFuture = ctx.writeAndFlush(data);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("读取一次消息中：" + (ResponseData) msg);
        ctx.close();
    }
}
