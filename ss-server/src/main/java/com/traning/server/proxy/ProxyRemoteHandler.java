package com.traning.server.proxy;

import com.traning.utils.ChannelUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

/**
 * 代理服务器解析
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:49
 */
public class ProxyRemoteHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 保存与客户端的入站处理器
     */
    private Channel inbound;

    public ProxyRemoteHandler(Channel inbound) {
        this.inbound = inbound;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!inbound.isActive()) {
            ChannelUtils.closeOnFlush(ctx.channel());
        } else {
            ctx.read();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelUtils.closeOnFlush(inbound);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        // 读取远程服务器的内容将其转发给客户端，这个转发可以是原封不动的
        ChannelUtils.msgForward(ctx, inbound, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ChannelUtils.closeOnFlush(ctx.channel());
    }

}
