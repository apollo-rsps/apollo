package org.apollo.net.session;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A session which is used as the attachment of a {@link ChannelHandlerContext}
 * in Netty.
 * @author Graham
 */
public abstract class Session {

	/**
	 * The channel.
	 */
	private final Channel channel;

	/**
	 * Creates a session for the specified channel.
	 * @param channel The channel.
	 */
	public Session(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Gets the channel.
	 * @return The channel.
	 */
	protected final Channel getChannel() {
		return channel;
	}

	/**
	 * Processes a message received from the channel.
	 * @param message The message.
	 * @throws Exception if an error occurs.
	 */
	public abstract void messageReceived(Object message) throws Exception;

	/**
	 * Destroys this session.
	 * @throws Exception if an error occurs.
	 */
	public abstract void destroy() throws Exception;

}
