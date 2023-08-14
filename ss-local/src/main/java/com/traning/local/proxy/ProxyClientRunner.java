package com.traning.local.proxy;

import com.google.common.net.HostAndPort;
import com.traning.runner.ClientRunner;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.Scanner;

/**
 * 客户端配置
 *
 * @author Wang Junwei
 * @date 2021/7/21
 * @see <a href="https://netty.io/wiki/user-guide-for-5.x.html">netty</a>
 */
public class ProxyClientRunner extends ClientRunner {

    private HostAndPort targetProxy;

    public static void main(String[] args) throws Exception {
        // 代理服务器地址
        HostAndPort hostAndPort = HostAndPort.fromString("127.0.0.1:8002");
        ProxyClientRunner proxyClient = new ProxyClientRunner();
        proxyClient.setHostAndPort(hostAndPort);
        proxyClient.start();
        ChannelFuture channelFuture = proxyClient.getChannelFuture();

        // 设置目标服务器地址
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入目标服务器地址：");
        String next = scanner.next();
        HostAndPort proxy = HostAndPort.fromString(next);
        proxyClient.setTargetProxy(proxy);

        // 执行一次服务访问
        channelFuture.channel().writeAndFlush("");
    }

    public HostAndPort getTargetProxy() {
        return targetProxy;
    }

    public void setTargetProxy(HostAndPort targetProxy) {
        this.targetProxy = targetProxy;
    }

    @Override
    public ChannelHandler[] createChannel(Channel channel) {
        return new ChannelHandler[] {

        };
    }
}
