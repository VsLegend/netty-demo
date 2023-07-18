package com.traning.local.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * 解析服务器返回数据
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:49
 */
public class ClientHttpReadHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpResponse request;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close().sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            request = (HttpResponse) msg;
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            String length = request.headers().get(HttpHeaderNames.CONTENT_LENGTH);
            ByteBuf byteBuf = content.content();
            CharSequence charSequence = byteBuf.getCharSequence(0, Integer.parseInt(length), StandardCharsets.UTF_8);
            System.out.println(charSequence);
        }
    }
}
