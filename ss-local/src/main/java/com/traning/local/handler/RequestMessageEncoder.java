package com.traning.local.handler;

import com.traning.domain.dto.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 消息数据编码器
 *
 * @author Wang Junwei
 * @date 2023/5/22 17:51
 */
public class RequestMessageEncoder extends MessageToByteEncoder<RequestData> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestData msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getId());
        out.writeInt(msg.getName().length());
        out.writeCharSequence(msg.getName(), StandardCharsets.UTF_8);
    }
}
