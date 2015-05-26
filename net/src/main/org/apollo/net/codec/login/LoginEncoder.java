package org.apollo.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * A {@link MessageToMessageEncoder} which encodes login response messages.
 *
 * @author Graham
 */
public final class LoginEncoder extends MessageToMessageEncoder<LoginResponse> {

	/**
	 * Creates the login encoder.
	 */
	public LoginEncoder() {
		super(LoginResponse.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponse response, List<Object> out) {
		ByteBuf buffer = ctx.alloc().buffer(3);
		buffer.writeByte(response.getStatus());

		if (response.getStatus() == LoginConstants.STATUS_OK) {
			buffer.writeByte(response.getRights());
			buffer.writeByte(response.isFlagged() ? 1 : 0);
		}

		out.add(buffer);
	}

}