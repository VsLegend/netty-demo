package com.traning.server.handler;

import com.traning.domain.dto.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 返回编码
 *
 * @author Wang Junwei
 * @date 2023/5/22 17:23
 */
public class ResponseMessageEncoder extends MessageToByteEncoder<ResponseData> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getId());
        out.writeBoolean(msg.getSuccess());
    }
}
