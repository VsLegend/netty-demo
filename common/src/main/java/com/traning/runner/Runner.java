package com.traning.runner;

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

    protected final EventLoopGroup worker;


    public Runner() {
        worker = new NioEventLoopGroup();
    }

    /**
     * 创建出入编码执行器，如果不能作为共享的解码编码处理器，那么不要设置为单例
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

    public void setHostAndPort(@Nonnull HostAndPort hostAndPort) {
        host = hostAndPort.getHost();
        port = hostAndPort.getPort();
    }

    public EventLoopGroup getWorker() {
        return worker;
    }
}
