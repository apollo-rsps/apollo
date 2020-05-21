package org.apollo.game.session;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

import org.apollo.ServerContext;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.update.UpdateDispatcher;

/**
 * An update session.
 *
 * @author Graham
 */
public final class UpdateSession extends Session {

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * Creates an update session for the specified channel.
	 *
	 * @param channel The channel.
	 * @param context The server context.
	 */
	public UpdateSession(Channel channel, ServerContext context) {
		super(channel);
		this.context = context;
	}

	@Override
	public void destroy() {
		channel.close();
	}

	@Override
	public void messageReceived(Object message) {
		UpdateDispatcher dispatcher = context.getUpdateService().getDispatcher();

		if (message instanceof OnDemandRequest) {
			dispatcher.dispatch(getChannel(), (OnDemandRequest) message);
		} else if (message instanceof JagGrabRequest) {
			dispatcher.dispatch(getChannel(), (JagGrabRequest) message);
		} else if (message instanceof HttpRequest) {
			dispatcher.dispatch(getChannel(), (HttpRequest) message);
		} else {
			throw new IllegalArgumentException("Unknown message type.");
		}
	}

}