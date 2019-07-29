package org.apollo.net.websocket;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.List;


public class WebSockBufferToByteBufDecoder extends MessageToMessageDecoder<WebSocketFrame> {

	private static final AttributeKey<RequestContext> WEBSOCKET_CONTEXT = AttributeKey.valueOf("websocket");

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		if(msg instanceof BinaryWebSocketFrame) {
			RequestContext requestContext = new RequestContext();
			requestContext.setWebsocketRequest(true);
			ctx.channel().attr(WEBSOCKET_CONTEXT).set(requestContext);

			out.add(Unpooled.copiedBuffer(msg.content()));
//			byte[] test = new byte[] {1, 2 ,3 ,4};
//			ctx.write(new BinaryWebSocketFrame(Unpooled.copiedBuffer(test)));
		} else if (msg instanceof CloseWebSocketFrame) {
			ctx.close();
		}

	}

}
