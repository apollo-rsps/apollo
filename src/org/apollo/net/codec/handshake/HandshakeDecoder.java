package org.apollo.net.codec.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.apollo.net.codec.login.LoginDecoder;
import org.apollo.net.codec.login.LoginEncoder;
import org.apollo.net.codec.update.UpdateDecoder;
import org.apollo.net.codec.update.UpdateEncoder;

/**
 * A {@link ByteToMessageDecoder} which decodes the handshake and makes changes to the pipeline as appropriate for the
 * selected service.
 * 
 * @author Graham
 */
public final class HandshakeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.isReadable()) {
			int id = buffer.readUnsignedByte();

			switch (id) {
			case HandshakeConstants.SERVICE_GAME:
				ctx.pipeline().addFirst("loginEncoder", new LoginEncoder());
				ctx.pipeline().addAfter("handshakeDecoder", "loginDecoder", new LoginDecoder());
				break;
			case HandshakeConstants.SERVICE_UPDATE:
				ctx.pipeline().addFirst("updateEncoder", new UpdateEncoder());
				ctx.pipeline().addBefore("handler", "updateDecoder", new UpdateDecoder());
				ByteBuf buf = ctx.alloc().buffer(8);
				buf.writeLong(0);
				ctx.channel().writeAndFlush(buf);
				break;
			default:
				throw new IllegalArgumentException("Invalid service id.");
			}

			ctx.pipeline().remove(this);
			HandshakeMessage message = new HandshakeMessage(id);

			out.add(message);
			if (buffer.isReadable()) {
				out.add(buffer.readBytes(buffer.readableBytes()));
			}
		}
	}

}