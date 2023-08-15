package com.traning.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Wang Junwei
 * @date 2023/8/14 16:52
 */
@Slf4j
public class ChannelUtils {

    /**
     * 关闭出入站通道
     *
     * @param ch
     */
    public static void closeOnFlush(Channel ch) {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 消息转发
     *
     * @param outbound
     * @param msg
     */
    public static void msgForward(Channel outbound, Object msg) {
        outbound.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("发送给id：{}成功", outbound.id());
            } else {
                log.error("发送给id：{}失败：{}", outbound.id(), future.cause());
                future.channel().close();
            }
        });
    }

    /**
     * 消息转发成功后接收方读取
     *
     * @param inCtx
     * @param outbound
     * @param msg
     */
    public static void msgForwardThenRead(ChannelHandlerContext inCtx, Channel outbound, Object msg) {
        outbound.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // 继续读取消息，确保消息转发不会中断，ChannelOption.AUTO_READ为false时才需要手动调用读取
                // 发送消息成功，开始读取接收方的信息
                log.debug("id：{}消息发送成功， id：{}开始读取数据", inCtx.channel().id(), outbound.id());
                inCtx.channel().read();
            } else {
                log.error("发送给id：{}失败：{}", outbound.id(), future.cause());
                future.channel().close();
            }
        });
    }
}
