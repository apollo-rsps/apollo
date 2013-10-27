package org.apollo.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.net.codec.handshake.HandshakeConstants;
import org.apollo.net.codec.handshake.HandshakeMessage;
import org.apollo.net.codec.jaggrab.JagGrabRequest;
import org.apollo.net.session.LoginSession;
import org.apollo.net.session.Session;
import org.apollo.net.session.UpdateSession;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * An implementation of {@link SimpleChannelUpstreamHandler} which handles
 * incoming upstream events from Netty.
 * @author Graham
 */
public final class ApolloHandler extends IdleStateAwareChannelUpstreamHandler {

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
	 * @param context The server context.
	 */
	public ApolloHandler(ServerContext context) {
		this.serverContext = context;
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		e.getChannel().close();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		logger.info("Channel connected: " + channel);
		serverContext.getChannelGroup().add(channel);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		logger.info("Channel disconnected: " + channel);
		serverContext.getChannelGroup().remove(channel);
		Object attachment = ctx.getAttachment();
		if (attachment != null) {
			((Session) attachment).destroy();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (ctx.getAttachment() == null) {
			Object msg = e.getMessage();
			if (msg instanceof HttpRequest || msg instanceof JagGrabRequest) {
				Session s = new UpdateSession(ctx.getChannel(), serverContext);
				s.messageReceived(msg);
				// we don't bother to set it as an attachment, as the connection
				// will be closed once the request is completed anyway
			} else {
				HandshakeMessage handshakeMessage = (HandshakeMessage) msg;
				switch (handshakeMessage.getServiceId()) {
				case HandshakeConstants.SERVICE_GAME:
					ctx.setAttachment(new LoginSession(ctx.getChannel(), ctx, serverContext));
					break;
				case HandshakeConstants.SERVICE_UPDATE:
					ctx.setAttachment(new UpdateSession(ctx.getChannel(), serverContext));
					break;
				default:
					throw new Exception("Invalid service id");
				}
			}
		} else {
			((Session) ctx.getAttachment()).messageReceived(e.getMessage());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		logger.log(Level.WARNING, "Exception occured for channel: " + e.getChannel() + ", closing...", e.getCause());
		ctx.getChannel().close();
	}

}
