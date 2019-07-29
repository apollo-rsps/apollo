package org.apollo.net.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.List;

public class ByteBufToWebSockBufferEncoder extends MessageToMessageEncoder<ByteBuf> {

	private static final AttributeKey<RequestContext> WEBSOCKET_CONTEXT = AttributeKey.valueOf("websocket");

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		RequestContext requestContext = ctx.channel().attr(WEBSOCKET_CONTEXT).get();
		if (requestContext != null && requestContext.isWebsocketRequest()) {
			out.add(new BinaryWebSocketFrame(Unpooled.copiedBuffer(msg)));
		}
	}
}
