package org.apollo.net;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocket13FrameEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apollo.net.codec.handshake.HandshakeDecoder;
import org.apollo.net.websocket.ByteBufToWebSockBufferEncoder;
import org.apollo.net.websocket.HttpServerHandler;
import org.apollo.net.websocket.WebSockBufferToByteBufDecoder;
import org.apollo.net.websocket.WebsocketSender;

/**
 * A {@link ChannelInitializer} for the service pipeline.
 *
 * @author Graham
 */
public final class WebSocketServiceChannelInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * The network event handler.
	 */
	private final ChannelInboundHandlerAdapter handler;

	/**
	 * Creates the service pipeline factory.
	 *
	 * @param handler The networking event handler.
	 */
	public WebSocketServiceChannelInitializer(ChannelInboundHandlerAdapter handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		pipeline.addLast("httpHandler", new HttpServerHandler());

		pipeline.addLast("handshakeDecoder", new HandshakeDecoder());
		pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.IDLE_TIME, 0, 0));
		pipeline.addLast("handler", handler);

		pipeline.addLast("byteBufToWebsocketEncoder", new ByteBufToWebSockBufferEncoder());
//		pipeline.addBefore("byteBufToWebsocketEncoder", "websocketEncoder", new WebSocket13FrameEncoder(false));
//		pipeline.addLast("websocketSender", new WebsocketSender());
	}

}