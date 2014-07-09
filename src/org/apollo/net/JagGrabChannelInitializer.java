package org.apollo.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;

import org.apollo.net.codec.jaggrab.JagGrabRequestDecoder;
import org.apollo.net.codec.jaggrab.JagGrabResponseEncoder;

/**
 * A {@link ChannelPipelineFactory} for the JAGGRAB protocol.
 * 
 * @author Graham
 */
public final class JagGrabChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * A buffer with two line feed (LF) characters in it.
     */
    private static final ByteBuf DOUBLE_LINE_FEED_DELIMITER = Unpooled.buffer(2);

    /**
     * The character set used in the request.
     */
    private static final Charset JAGGRAB_CHARSET = Charset.forName("US-ASCII");

    /**
     * The maximum length of a request, in bytes.
     */
    private static final int MAX_REQUEST_LENGTH = 8192;

    /**
     * Populates the double line feed buffer.
     */
    static {
	DOUBLE_LINE_FEED_DELIMITER.writeByte(10).writeByte(10);
    }

    /**
     * The file server event handler.
     */
    private final ApolloHandler handler;

    /**
     * Creates a {@code JAGGRAB} pipeline factory.
     * 
     * @param handler The file server event handler.
     */
    public JagGrabChannelInitializer(ApolloHandler handler) {
	this.handler = handler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
	ChannelPipeline pipeline = ch.pipeline();

	// decoders
	pipeline.addLast("framer", new DelimiterBasedFrameDecoder(MAX_REQUEST_LENGTH, DOUBLE_LINE_FEED_DELIMITER));
	pipeline.addLast("string-decoder", new StringDecoder(JAGGRAB_CHARSET));
	pipeline.addLast("jaggrab-decoder", new JagGrabRequestDecoder());

	// encoders
	pipeline.addLast("jaggrab-encoder", new JagGrabResponseEncoder());

	// handler
	pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.IDLE_TIME, 0, 0));
	pipeline.addLast("handler", handler);
    }

}