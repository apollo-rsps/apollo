package org.apollo.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * A {@link ChannelPipelineFactory} for the HTTP protocol.
 * 
 * @author Graham
 */
public final class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * The maximum length of a request, in bytes.
     */
    private static final int MAX_REQUEST_LENGTH = 8192;

    /**
     * The server event handler.
     */
    private final ApolloHandler handler;

    /**
     * Creates the HTTP pipeline factory.
     * 
     * @param handler The file server event handler.
     */
    public HttpChannelInitializer(ApolloHandler handler) {
	this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();

	// decoders
	pipeline.addLast("decoder", new HttpRequestDecoder());
	pipeline.addLast("chunker", new HttpObjectAggregator(MAX_REQUEST_LENGTH));

	// encoders
	pipeline.addLast("encoder", new HttpResponseEncoder());

	// handler
	pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.IDLE_TIME, 0, 0));
	pipeline.addLast("handler", handler);
    }

}