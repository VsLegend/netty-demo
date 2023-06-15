package com.traning.server.http;

import com.google.common.net.HostAndPort;
import com.traning.runner.ServerRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * http请求
 *
 * @author Wang Junwei
 * @date 2023/6/15 10:29
 */
public class HttpServerRunner extends ServerRunner {

    public static void main(String[] args) throws Exception {
        HttpServerRunner runner = new HttpServerRunner();
        runner.setPort(8099);
        runner.start();
    }

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[] {
                new HttpServerCodec(),
                new HttpContentCompressor((CompressionOptions[]) null),
                new HttpServerExpectContinueHandler()
        };
    }
}
