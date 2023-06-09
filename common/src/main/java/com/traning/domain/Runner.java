package com.traning.domain;

import com.google.common.net.HostAndPort;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 服务包装
 *
 * @author Wang Junwei
 * @date 2023/6/9 11:54
 */
public abstract class Runner {

    protected String host;

    protected Integer port;

    protected HostAndPort hostAndPort;

    protected final EventLoopGroup worker;

    protected ChannelHandler[] channelHandlers;


    public Runner() {
        worker = new NioEventLoopGroup();
    }

    /**
     * 创建出入编码执行器
     *
     * @return
     */
    public abstract ChannelHandler[] createChannelHandlers();

    /**
     * 启动服务
     *
     * @throws Exception
     */
    public abstract void start() throws Exception;

    /**
     * 关闭服务
     *
     * @throws Exception
     */
    public abstract void stop() throws Exception;

    /**
     * 运行前校验
     */
    public void startCheck() {
        Objects.requireNonNull(hostAndPort);
        Objects.requireNonNull(host);
        Objects.requireNonNull(port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ChannelHandler[] getChannelHandlers() {
        return channelHandlers;
    }

    public void setHostAndPort(@Nonnull HostAndPort hostAndPort) {
        this.hostAndPort = hostAndPort;
        host = hostAndPort.getHost();
        port = hostAndPort.getPort();
    }

    public HostAndPort getHostAndPort() {
        return hostAndPort;
    }
}
