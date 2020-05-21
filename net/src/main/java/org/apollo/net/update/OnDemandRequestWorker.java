package org.apollo.net.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apollo.cache.Cache;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.codec.update.OnDemandResponse;

import java.io.IOException;

/**
 * A worker which services 'on-demand' requests.
 *
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest, Cache> {

	/**
	 * Creates the 'on-demand' request worker.
	 *
	 * @param dispatcher The dispatcher.
	 * @param fs         The file system.
	 */
	public OnDemandRequestWorker(UpdateDispatcher dispatcher, Cache fs) {
		super(dispatcher, fs);
	}

	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextOnDemandRequest();
	}

	@Override
	protected void service(Cache cache, Channel channel, OnDemandRequest request) throws IOException {
		final var fs = request.getFs();
		final var folder = request.getFolder();

		ByteBuf buf;
		if (fs == 255 && folder == 255) {
			buf = Unpooled.wrappedBuffer(cache.generateInformationStoreDescriptor(false).getBuffer());
		} else {
			final var store = fs == 255 ? cache.getMasterIndex() : cache.getIndex(fs);
			if (store == null) {
				return;
			}
			buf = Unpooled.wrappedBuffer(store.get(folder).getBuffer());
			if (fs != 255) buf = buf.slice(0, buf.readableBytes() - 2);
		}

		OnDemandResponse response = new OnDemandResponse(fs, folder, request.getPriority(), buf);
		channel.writeAndFlush(response);
	}
}