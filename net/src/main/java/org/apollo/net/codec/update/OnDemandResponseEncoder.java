package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * A {@link MessageToMessageEncoder} for the 'on-demand' protocol.
 *
 * @author Graham
 */
public final class OnDemandResponseEncoder extends MessageToByteEncoder<OnDemandResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OnDemandResponse response, ByteBuf buf) {
		int fs = response.getFs();
		int folder = response.getFolder();
		ByteBuf container = response.getChunkData();

		buf.writeByte(fs);
		buf.writeShort(folder);

		int compression = container.readUnsignedByte();
		if (response.getPriority() == OnDemandRequest.Priority.LOW) {
			compression |= 0x80;
		}

		buf.writeByte(compression);

		int bytes = container.readableBytes();
		if (bytes > 508) bytes = 508;

		buf.writeBytes(container.readBytes(bytes));

		for (; ; ) {
			bytes = container.readableBytes();
			if (bytes == 0) {
				break;
			} else if (bytes > 511) bytes = 511;

			buf.writeByte(0xFF);

			final var payload = container.readBytes(bytes);
			buf.writeBytes(payload);

			payload.release();
		}
	}
}