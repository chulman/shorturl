package com.chulm.test.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.compression.FastLzFrameDecoder;

import java.util.List;

public class MessageDecoder extends FastLzFrameDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readBytes(in.readableBytes()));
    }

}