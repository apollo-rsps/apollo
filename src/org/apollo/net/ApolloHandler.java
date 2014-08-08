package org.apollo.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

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
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (ctx.attr(NetworkConstants.SESSION_KEY).get() == null) {
			if (msg instanceof HttpRequest || msg instanceof JagGrabRequest) {
				new UpdateSession(ctx.channel(), serverContext).messageReceived(msg);
			} else {
				HandshakeMessage handshakeMessage = (HandshakeMessage) msg;
				switch (handshakeMessage.getServiceId()) {
				case HandshakeConstants.SERVICE_GAME:
					ctx.attr(NetworkConstants.SESSION_KEY).set(new LoginSession(ctx, serverContext));
					break;
				case HandshakeConstants.SERVICE_UPDATE:
					ctx.attr(NetworkConstants.SESSION_KEY).set(new UpdateSession(ctx.channel(), serverContext));
					break;
				default:
					throw new IllegalStateException("Invalid service id.");
				}
			}
		} else {
			ctx.attr(NetworkConstants.SESSION_KEY).get().messageReceived(msg);
		}
	}

}