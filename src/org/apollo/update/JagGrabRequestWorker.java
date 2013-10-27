package org.apollo.update;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.codec.jaggrab.JagGrabResponse;
import org.apollo.update.resource.ResourceProvider;
import org.apollo.update.resource.VirtualResourceProvider;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A worker which services JAGGRAB requests.
 * @author Graham
 */
public final class JagGrabRequestWorker extends RequestWorker<JagGrabRequest, ResourceProvider> {

	/**
	 * Creates the JAGGRAB request worker.
	 * @param dispatcher The dispatcher.
	 * @param fs The file system.
	 */
	public JagGrabRequestWorker(UpdateDispatcher dispatcher, IndexedFileSystem fs) {
		super(dispatcher, new VirtualResourceProvider(fs));
	}

	@Override
	protected ChannelRequest<JagGrabRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextJagGrabRequest();
	}

	@Override
	protected void service(ResourceProvider provider, Channel channel, JagGrabRequest request) throws IOException {
		ByteBuffer buf = provider.get(request.getFilePath());
		if (buf == null) {
			channel.close();
		} else {
			ChannelBuffer wrapped = ChannelBuffers.wrappedBuffer(buf);
			channel.write(new JagGrabResponse(wrapped)).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
