package org.apollo.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.IOException;

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
	 * The maximum length of a chunk, in {@code byte}s.
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
		ByteBuf buffer = Unpooled.wrappedBuffer(fs.getFile(request.getFileDescriptor()));
		int length = buffer.readableBytes();

		for (int chunk = 0; buffer.readableBytes() > 0; chunk++) {
			int chunkSize = Math.min(buffer.readableBytes(), CHUNK_LENGTH);
			OnDemandResponse response = new OnDemandResponse(request.getFileDescriptor(), length, chunk, buffer.readBytes(chunkSize));
			channel.writeAndFlush(response);
		}
	}

}