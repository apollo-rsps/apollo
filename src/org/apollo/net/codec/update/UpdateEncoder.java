package org.apollo.net.codec.update;

import org.apollo.fs.FileDescriptor;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} for the 'on-demand' protocol.
 * 
 * @author Graham
 */
public final class UpdateEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) {
		if (msg instanceof OnDemandResponse) {
			OnDemandResponse response = (OnDemandResponse) msg;

			FileDescriptor descriptor = response.getFileDescriptor();
			int fileSize = response.getFileSize();
			int chunkId = response.getChunkId();
			ChannelBuffer chunkData = response.getChunkData();

			ChannelBuffer buffer = ChannelBuffers.buffer(6 + chunkData.readableBytes());
			buffer.writeByte(descriptor.getType() - 1);
			buffer.writeShort(descriptor.getFile());
			buffer.writeShort(fileSize);
			buffer.writeByte(chunkId);
			buffer.writeBytes(chunkData);

			return buffer;
		}
		return msg;
	}

}