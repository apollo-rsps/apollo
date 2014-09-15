package org.apollo.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * A session which is used as an attribute of a {@link ChannelHandlerContext} in Netty.
 * 
 * @author Graham
 */
public abstract class Session {

	/**
	 * The channel.
	 */
	private final Channel channel;

	/**
	 * Creates a session for the specified channel.
	 * 
	 * @param channel The channel.
	 */
	public Session(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Destroys this session.
	 */
	public abstract void destroy();

	/**
	 * Gets the channel.
	 * 
	 * @return The channel.
	 */
	protected final Channel getChannel() {
		return channel;
	}

	/**
	 * Processes a message received from the channel.
	 * 
	 * @param message The message.
	 */
	public abstract void messageReceived(Object message);

}