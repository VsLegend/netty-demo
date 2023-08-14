package com.traning.server.proxy;

import com.traning.utils.ChannelUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

/**
 * @author Wang Junwei
 * @date 2023/8/14 15:17
 */
public class ProxyLocalHandler extends SimpleChannelInboundHandler<HttpObject> {

    private final String remoteHost;
    private final Integer remotePort;

    /**
     * 保存与远程服务的出站处理器，到时候可以根据客户端状态来选择是否接收或关闭与远程服务的连接
     */
    private Channel outbound;

    public ProxyLocalHandler(String remoteHost, Integer remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ProxyRemoteClientRunner remoteClientRunner = new ProxyRemoteClientRunner(remoteHost, remotePort, ctx);
        outbound = remoteClientRunner.connect();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 关闭连接时，确保与远程服务器的连接也被关闭
        ChannelUtils.closeOnFlush(outbound);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 在转发消失时，需要时刻检测代理服务器与远程服务器的连接是激活状态
        if (outbound.isActive()) {
            // 代理访问
            if (msg instanceof HttpRequest) {
                // 消息头这里可以适当的隐藏原请求的内容，比如请求头的User-Agent等内容，也可以修改X-Forwarded-For来隐藏真实IP地址
                HttpRequest httpRequest = (HttpRequest) msg;
                DefaultHttpRequest proxyRequest = new DefaultHttpRequest(httpRequest.protocolVersion(), httpRequest.method(), httpRequest.uri());
                proxyRequest.headers().setAll(httpRequest.headers());
                // 消息转发
                ChannelUtils.msgForward(ctx, outbound, proxyRequest);
            } else if (msg instanceof HttpContent) {
                // 消息体就原封不动的转发就行
                ChannelUtils.msgForward(ctx, outbound, msg);
            }
            // 忽略其他消息
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 确保异常退出时，关闭连接
        ChannelUtils.closeOnFlush(ctx.channel());
    }
}
