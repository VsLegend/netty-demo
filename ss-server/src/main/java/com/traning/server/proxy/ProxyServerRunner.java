package com.traning.server.proxy;

import com.traning.runner.ServerRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * 代理服务器
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:12
 */
public class ProxyServerRunner extends ServerRunner {

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[0];
    }
}
