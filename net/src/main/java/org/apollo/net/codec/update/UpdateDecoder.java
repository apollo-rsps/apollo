package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apollo.net.codec.update.OnDemandRequest.Priority;

import java.util.List;

/**
 * A {@link ByteToMessageDecoder} for the 'on-demand' protocol.
 *
 * @author Graham
 */
public final class UpdateDecoder extends ByteToMessageDecoder {

	private enum State {
		READ_VERSION, READ_REQUEST
	}

	private State state = State.READ_VERSION;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (!buffer.isReadable( Byte.BYTES + Byte.BYTES + Short.BYTES)) {
			return;
		}

		if (state == State.READ_VERSION) {
			this.state = State.READ_REQUEST;
			out.add(new OnDemandInfo(buffer.readUnsignedInt()));
		} else {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0 || opcode == 1) {
				out.add(new OnDemandRequest(buffer.readUnsignedByte(), buffer.readUnsignedShort(),
						Priority.valueOf(opcode)));
			} else if (opcode == 4) {
				int key = buffer.readUnsignedByte();
				buffer.readerIndex(buffer.readerIndex() + Short.BYTES);
			} else {
				buffer.readerIndex(buffer.readerIndex() + Byte.BYTES + Short.BYTES);
			}
		}
	}
}