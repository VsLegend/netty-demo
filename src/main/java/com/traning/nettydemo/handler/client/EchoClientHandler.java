package com.traning.nettydemo.handler.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Author Wong Jwei
 * @Date 2021/7/21
 * @Description 客户端处理器
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理接收到的数据
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            while (byteBuf.isReadable()) {
                System.out.print((char) byteBuf.readByte());
            }
            System.out.println();
        } finally {
            ReferenceCountUtil.release(msg); // 直接释放资源
            // byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
