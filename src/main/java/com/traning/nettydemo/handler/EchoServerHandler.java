package com.traning.nettydemo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * @Author Wong Jwei
 * @Date 2021/7/20
 * @Description 应答服务器数据处理器
 */

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 在连接被建立并且准备进行通信时被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer(4);
        String content = "Hello Netty!";
        byteBuf.writeBytes(content.getBytes());

        // 请求操作都不会马上被执行，因为在 Netty 里所有的操作都是异步的
        ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf); // 一个 ChannelFuture 代表了一个还没有发生的 I/O 操作
        // 添加一个ChannelFuture的监听器，用以监听ChannelFuture何时完成，并在完成时做相应的操作
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                assert channelFuture == future;
//                ctx.close();
            }
        });
    }


    // 在连接完毕，收到消息时调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            ByteBuf byteBuf =  (ByteBuf) msg;
//            byte c = byteBuf.readByte();
//            System.out.println(c);
            ctx.writeAndFlush("Msg received");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
