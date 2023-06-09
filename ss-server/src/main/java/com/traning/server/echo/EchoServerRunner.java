package com.traning.server.echo;

import com.google.common.net.HostAndPort;
import com.traning.runner.ServerRunner;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 服务端配置
 *
 * @author Wang Junwei
 * @date 2021/7/20
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class EchoServerRunner extends ServerRunner {

    public static void main(String[] args) throws Exception {
        HostAndPort hostAndPort = HostAndPort.fromString("localhost:7021");
        EchoServerRunner server = new EchoServerRunner();
        server.setHostAndPort(hostAndPort);
        server.start();
    }

    @Override
    public ChannelHandler[] createChannelHandlers() {
        return new ChannelHandler[]{
                new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()),
                new StringDecoder(), new StringEncoder(), new ServerProcessHandler()
        };
    }
}
