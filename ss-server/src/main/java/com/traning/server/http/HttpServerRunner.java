package com.traning.server.http;

import com.traning.runner.ServerRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * http请求
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:29
 */
public class HttpServerRunner extends ServerRunner {

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[] {

        };
    }
}
