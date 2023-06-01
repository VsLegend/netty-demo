package com.traning.server.handler;

import com.traning.domain.dto.RequestData;
import com.traning.domain.dto.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

/**
 * 事件处理器
 *
 * @author Wang Junwei
 * @date 2023/5/22 16:48
 */
public class ServerProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = "Reply: This is reply from server-.^";
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeCharSequence(message, StandardCharsets.UTF_8);
        ChannelFuture channelFuture = ctx.write(buffer);
//        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        channelFuture.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println("消息回复成功：" + message);
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

}