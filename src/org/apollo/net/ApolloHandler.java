package org.apollo.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCountUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.net.codec.handshake.HandshakeConstants;
import org.apollo.net.codec.handshake.HandshakeMessage;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.session.LoginSession;
import org.apollo.net.session.Session;
import org.apollo.net.session.UpdateSession;

/**
 * An implementation of {@link ChannelInboundHandlerAdapter} which handles incoming upstream events from Netty.
 * 
 * @author Graham
 */
@Sharable
public final class ApolloHandler extends ChannelInboundHandlerAdapter {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ApolloHandler.class.getName());

	/**
	 * The server context.
	 */
	private final ServerContext serverContext;

	/**
	 * Creates the Apollo event handler.
	 * 
	 * @param context The server context.
	 */
	public ApolloHandler(ServerContext context) {
		serverContext = context;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		Session session = ctx.attr(NetworkConstants.SESSION_KEY).getAndRemove();
		if (session != null) {
			session.destroy();
		}
		logger.fine("Channel disconnected: " + channel);
		channel.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (!e.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
			logger.log(Level.WARNING, "Exception occured for channel: " + ctx.channel() + ", closing...", e);
		}
		ctx.channel().close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) {
		try {
			Attribute<Session> attr = ctx.attr(NetworkConstants.SESSION_KEY);

			Session session = attr.get();

			if (session != null) {
				session.messageReceived(message);
				return;
			}

			if (message instanceof HttpRequest || message instanceof JagGrabRequest) {
				session = new UpdateSession(ctx.channel(), serverContext);
			}

			// TODO: Perhaps let HandshakeMessage implement Message to remove this explicit check
			if (message instanceof HandshakeMessage) {
				HandshakeMessage handshakeMessage = (HandshakeMessage) message;

				switch (handshakeMessage.getServiceId()) {
				case HandshakeConstants.SERVICE_GAME:
					attr.set(new LoginSession(ctx, serverContext));
					break;

				case HandshakeConstants.SERVICE_UPDATE:
					attr.set(new UpdateSession(ctx.channel(), serverContext));
					break;
				}
			}

		} finally {
			ReferenceCountUtil.release(message);
		}
	}

}