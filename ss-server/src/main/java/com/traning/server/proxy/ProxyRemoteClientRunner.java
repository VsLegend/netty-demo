package com.traning.server.proxy;

import com.google.common.net.HostAndPort;
import com.traning.runner.ClientRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * 代理服务器的远程请求客户端
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:13
 */
public class ProxyRemoteClientRunner extends ClientRunner {

    public static void main(String[] args) {
        HostAndPort port = HostAndPort.fromString("https://www.baidu.com/");

    }

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[0];
    }
}
