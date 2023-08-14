package com.traning.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Wang Junwei
 * @date 2023/8/14 16:52
 */
public class ChannelUtils {

    /**
     * 关闭出入站通道
     * @param ch
     */
    public static void closeOnFlush(Channel ch) {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 消息转发
     * @param ctx
     * @param msg
     */
    public static void msgForward(ChannelHandlerContext inCtx, Channel outbound, Object msg) {
        outbound.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // 继续读取消息，确保消息转发不会中断
                inCtx.channel().read();
            } else {
                future.channel().close();
            }
        });
    }
}
