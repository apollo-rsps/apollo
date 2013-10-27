package org.apollo.net;

import org.apollo.net.codec.handshake.HandshakeDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.Timer;

/**
 * A {@link ChannelPipelineFactory} which creates {@link ChannelPipeline}s for
 * the service pipeline.
 * @author Graham
 */
public final class ServicePipelineFactory implements ChannelPipelineFactory {

	/**
	 * The network event handler.
	 */
	private final ApolloHandler handler;

	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer;

	/**
	 * Creates the service pipeline factory.
	 * @param handler The networking event handler.
	 * @param timer The timer used for idle checking.
	 */
	public ServicePipelineFactory(ApolloHandler handler, Timer timer) {
		this.handler = handler;
		this.timer = timer;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("handshakeDecoder", new HandshakeDecoder());
		pipeline.addLast("timeout", new IdleStateHandler(timer, NetworkConstants.IDLE_TIME, 0, 0));
		pipeline.addLast("handler", handler);
		return pipeline;
	}

}
