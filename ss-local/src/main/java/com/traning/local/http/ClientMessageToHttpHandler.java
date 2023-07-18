package com.traning.local.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * 将客户端发送的字符串消息都转发为HTTP请求消息
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:49
 */
public class ClientMessageToHttpHandler extends MessageToMessageEncoder<String> {

    private static final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) {
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/get", Unpooled.wrappedBuffer(CONTENT));
        // 消息发送完毕后关闭连接
        httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        ctx.writeAndFlush(httpRequest);
    }
}
