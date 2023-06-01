package com.traning.local.handler;

import com.traning.domain.dto.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 消息数据解吗器
 *
 * @author Wang Junwei
 * @date 2023/5/22 17:53
 */
public class ResponseMessageDecoder extends ReplayingDecoder<ResponseData> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setId(in.readInt());
        responseData.setSuccess(in.readBoolean());
        out.add(responseData);
    }
}
