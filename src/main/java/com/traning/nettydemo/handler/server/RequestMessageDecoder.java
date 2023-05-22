package com.traning.nettydemo.handler.server;

import com.traning.nettydemo.handler.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 请求解码
 *
 * @author Wang Junwei
 * @date 2023/5/22 17:17
 */
public class RequestMessageDecoder extends ReplayingDecoder<RequestData> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RequestData data = new RequestData();
        data.setId(in.readInt());
        int length = in.readInt();
        data.setName(in.readCharSequence(length, StandardCharsets.UTF_8).toString());
        out.add(data);
    }
}
