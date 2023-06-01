package com.traning.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 初始化请求通信事件
 *
 * @author Wang Junwei
 * @date 2023/5/22 17:36
 */
public class ServerRequestInitHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 初始化该通道的出入处理器，指定相关的出入规则
        ch.pipeline().addLast(new ServerProcessHandler());
//        ch.pipeline().addLast(new RequestMessageDecoder(), new ResponseMessageEncoder(), new ServerProcessHandler());
    }
}
