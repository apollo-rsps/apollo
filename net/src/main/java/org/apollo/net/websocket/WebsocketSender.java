package org.apollo.net.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WebsocketSender extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(msg instanceof ByteBuf) {
			ctx.channel().writeAndFlush(new BinaryWebSocketFrame((ByteBuf)msg));
		}
	}
}
