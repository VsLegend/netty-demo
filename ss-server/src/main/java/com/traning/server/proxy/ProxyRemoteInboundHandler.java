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
public class ProxyRemoteInboundHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 保存与客户端的入站处理器
     */
    private final Channel inbound;

    private HttpObject[] httpObjects;

    public ProxyRemoteInboundHandler(Channel inbound) {
        this.inbound = inbound;
        this.httpObjects = new HttpObject[1];
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
        ChannelUtils.msgForwardThenRead(ctx, inbound, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        inbound.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ChannelUtils.closeOnFlush(ctx.channel());
    }

}
