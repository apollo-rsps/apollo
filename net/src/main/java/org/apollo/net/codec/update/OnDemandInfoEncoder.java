package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OnDemandInfoEncoder extends MessageToByteEncoder<OnDemandInfoResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OnDemandInfoResponse msg, ByteBuf buf) throws Exception {
		buf.writeByte(msg.getResponse());
	}
}
