package com.traning.nettydemo.handler.server;

import com.traning.nettydemo.handler.RequestData;
import com.traning.nettydemo.handler.ResponseData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 事件处理器
 *
 * @author Wang Junwei
 * @date 2023/5/22 16:48
 */
public class ServerProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("读取一次消息中：" + msg);
        // 通过Decoder转为RequestData
        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        responseData.setId(requestData.getId());
        responseData.setSuccess(true);
        // 将ResponseData输出为指定流格式，并输出
        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
