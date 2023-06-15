package com.traning.local.echo;

import com.google.common.net.HostAndPort;
import com.traning.local.handler.ClientStringHandler;
import com.traning.runner.ClientRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户端配置
 *
 * @author Wang Junwei
 * @date 2021/7/21
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class EchoClientRunner extends ClientRunner {

    public static void main(String[] args) throws Exception {
        HostAndPort hostAndPort = HostAndPort.fromString("127.0.0.1:7021");
        EchoClientRunner client = new EchoClientRunner();
        client.setHostAndPort(hostAndPort);
        client.start();
        ChannelFuture channelFuture = client.getChannelFuture();
        channelFuture.channel().writeAndFlush("Hello!");
    }

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[]{
                new StringDecoder(),
                new ClientStringHandler(),
                new StringEncoder()
        };
    }
}
