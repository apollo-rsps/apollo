package org.apollo.net.codec.handshake;

import org.apollo.net.codec.login.LoginDecoder;
import org.apollo.net.codec.login.LoginEncoder;
import org.apollo.net.codec.update.UpdateDecoder;
import org.apollo.net.codec.update.UpdateEncoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * A {@link FrameDecoder} which decodes the handshake and makes changes to the
 * pipeline as appropriate for the selected service.
 * @author Graham
 */
public final class HandshakeDecoder extends FrameDecoder {

	/**
	 * Creates the handshake frame decoder.
	 */
	public HandshakeDecoder() {
		super(true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			int id = buffer.readUnsignedByte();

			switch (id) {
			case HandshakeConstants.SERVICE_GAME:
				ctx.getPipeline().addFirst("loginEncoder", new LoginEncoder());
				ctx.getPipeline().addBefore("handler", "loginDecoder", new LoginDecoder());
				break;
			case HandshakeConstants.SERVICE_UPDATE:
				ctx.getPipeline().addFirst("updateEncoder", new UpdateEncoder());
				ctx.getPipeline().addBefore("handler", "updateDecoder", new UpdateDecoder());
				ChannelBuffer buf = ChannelBuffers.buffer(8);
				buf.writeLong(0);
				channel.write(buf); // TODO should it be here?
				break;
			default:
				throw new Exception("Invalid service id");
			}

			ctx.getPipeline().remove(this);

			HandshakeMessage message = new HandshakeMessage(id);

			if (buffer.readable()) {
				return new Object[] { message, buffer.readBytes(buffer.readableBytes()) };
			} else {
				return message;
			}

		}
		return null;
	}

}
