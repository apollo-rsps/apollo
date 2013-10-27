package org.apollo.net.session;

import org.apollo.ServerContext;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.update.UpdateDispatcher;
import org.apollo.update.UpdateService;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * An update session.
 * @author Graham
 */
public final class UpdateSession extends Session {

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * Creates an update session for the specified channel.
	 * @param channel The channel.
	 * @param context The server context.
	 */
	public UpdateSession(Channel channel, ServerContext context) {
		super(channel);
		this.context = context;
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		UpdateDispatcher dispatcher = context.getService(UpdateService.class).getDispatcher();
		if (message instanceof OnDemandRequest) {
			dispatcher.dispatch(getChannel(), (OnDemandRequest) message);
		} else if (message instanceof JagGrabRequest) {
			dispatcher.dispatch(getChannel(), (JagGrabRequest) message);
		} else if (message instanceof HttpRequest) {
			dispatcher.dispatch(getChannel(), (HttpRequest) message);
		} else {
			throw new Exception("unknown message type");
		}
	}

	@Override
	public void destroy() throws Exception {
		// TODO implement
	}

}
