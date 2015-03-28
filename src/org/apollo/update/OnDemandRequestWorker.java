package org.apollo.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.FileDescriptor;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.codec.update.OnDemandResponse;

/**
 * A worker which services 'on-demand' requests.
 * 
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest, IndexedFileSystem> {

	/**
	 * The maximum length of a chunk, in bytes.
	 */
	private static final int CHUNK_LENGTH = 500;

	/**
	 * Creates the 'on-demand' request worker.
	 * 
	 * @param dispatcher The dispatcher.
	 * @param fs The file system.
	 */
	public OnDemandRequestWorker(UpdateDispatcher dispatcher, IndexedFileSystem fs) {
		super(dispatcher, fs);
	}

	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextOnDemandRequest();
	}

	@Override
	protected void service(IndexedFileSystem fs, Channel channel, OnDemandRequest request) throws IOException {
		FileDescriptor desc = request.getFileDescriptor();

		ByteBuffer buffer = fs.getFile(desc);
		int length = buffer.remaining();

		for (int chunk = 0; buffer.remaining() > 0; chunk++) {
			int chunkSize = Math.min(buffer.remaining(), CHUNK_LENGTH);

			byte[] data = new byte[chunkSize];
			buffer.get(data, 0, data.length);
			ByteBuf chunkData = Unpooled.wrappedBuffer(data, 0, chunkSize);

			OnDemandResponse response = new OnDemandResponse(desc, length, chunk, chunkData);
			channel.writeAndFlush(response);
		}
	}

}