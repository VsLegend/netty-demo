package com.traning.local.http;

import io.netty.channel.*;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Wang Junwei
 * @date 2023/6/15 10:49
 */
public class ClientHttpMessageHandler extends MessageToMessageEncoder<String> {

    private static final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        // 将客户端发送的消息都转发为HTTP请求消息
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        HttpRequest httpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, address.getHostString());
    }
}
