package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * A {@link MessageToMessageEncoder} for the 'on-demand' protocol.
 *
 */
public final class OnDemandResponseEncoder extends MessageToByteEncoder<OnDemandResponse> {

	/**
	 * Flag which denotes the request is async.
	 */
	private static final int PREFTECH = 0x80;

	/**
	 * The maximum number of bytes the initial payload can have. Includes the header.
	 */
	private static final int ENTRY_PAYLOAD_START = 508;

	/**
	 * The maximum number of bytes per entry.
	 */
	private static final int ENTRY_PAYLOAD = 511;

	/**
	 * Marks the beginning of the entry.
	 */
	private static final int ENTRY_DELIMITER = 0xFF;

	@Override
	protected void encode(ChannelHandlerContext ctx, OnDemandResponse response, ByteBuf buf) {
		int fs = response.getFs();
		int folder = response.getFolder();
		ByteBuf container = response.getChunkData();

		buf.writeByte(fs);
		buf.writeShort(folder);

		int compression = container.readUnsignedByte();
		if (response.getPriority() == OnDemandRequest.Priority.LOW) {
			compression |= PREFTECH;
		}

		buf.writeByte(compression);
		buf.writeBytes(container.readBytes(Math.min(ENTRY_PAYLOAD_START, container.readableBytes())));

		int bytes;
		while ((bytes = Math.min(ENTRY_PAYLOAD, container.readableBytes())) != 0) {
			buf.writeByte(ENTRY_DELIMITER);

			final var payload = container.readBytes(bytes);
			buf.writeBytes(payload);

			payload.release();
		}
	}
}