package org.apollo.net.session;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.model.Player;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.login.LoginService;
import org.apollo.net.ApolloHandler;
import org.apollo.net.codec.game.GameEventDecoder;
import org.apollo.net.codec.game.GameEventEncoder;
import org.apollo.net.codec.game.GamePacketDecoder;
import org.apollo.net.codec.game.GamePacketEncoder;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.codec.login.LoginResponse;
import org.apollo.net.release.Release;
import org.apollo.security.IsaacRandomPair;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A login session.
 * @author Graham
 */
public final class LoginSession extends Session {

	/**
	 * The context of the {@link ApolloHandler}.
	 */
	private final ChannelHandlerContext channelContext;

	/**
	 * The server context.
	 */
	private final ServerContext serverContext;

	/**
	 * Creates a login session for the specified channel.
	 * @param channel The channel.
	 * @param channelContext The context of the {@link ApolloHandler}.
	 * @param serverContext The server context.
	 */
	public LoginSession(Channel channel, ChannelHandlerContext channelContext, ServerContext serverContext) {
		super(channel);
		this.channelContext = channelContext;
		this.serverContext = serverContext;
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		if (message.getClass() == LoginRequest.class) {
			handleLoginRequest((LoginRequest) message);
		}
	}

	/**
	 * Gets the release.
	 * @return The release.
	 */
	public Release getRelease() {
		return serverContext.getRelease();
	}

	/**
	 * Handles a login request.
	 * @param request The login request.
	 */
	private void handleLoginRequest(LoginRequest request) {
		LoginService loginService = serverContext.getService(LoginService.class);
		loginService.submitLoadRequest(this, request);
	}

	/**
	 * Handles a response from the login service.
	 * @param request The request this response corresponds to.
	 * @param response The response.
	 */
	public void handlePlayerLoaderResponse(LoginRequest request, PlayerLoaderResponse response) {
		GameService gameService = serverContext.getService(GameService.class);
		Channel channel = getChannel();

		int status = response.getStatus();
		Player player = response.getPlayer();
		int rights = player == null ? 0 : player.getPrivilegeLevel().toInteger();
		boolean log = false;

		if (player != null) {
			GameSession session = new GameSession(channel, serverContext, player);
			player.setSession(session, false /* TODO */);

			RegistrationStatus registrationStatus = gameService.registerPlayer(player);

			if (registrationStatus != RegistrationStatus.OK) {
				player = null;
				if (registrationStatus == RegistrationStatus.ALREADY_ONLINE) {
					status = LoginConstants.STATUS_ACCOUNT_ONLINE;
				} else {
					status = LoginConstants.STATUS_SERVER_FULL;
				}
				rights = 0;
			}
		}

		ChannelFuture future = channel.write(new LoginResponse(status, rights, log));

		destroy();

		if (player != null) {
			IsaacRandomPair randomPair = request.getRandomPair();
			Release release = serverContext.getRelease();

			channel.getPipeline().addFirst("eventEncoder", new GameEventEncoder(release));
			channel.getPipeline().addBefore("eventEncoder", "gameEncoder", new GamePacketEncoder(randomPair.getEncodingRandom()));

			channel.getPipeline().addBefore("handler", "gameDecoder", new GamePacketDecoder(randomPair.getDecodingRandom(), serverContext.getRelease()));
			channel.getPipeline().addAfter("gameDecoder", "eventDecoder", new GameEventDecoder(release));

			channel.getPipeline().remove("loginDecoder");
			channel.getPipeline().remove("loginEncoder");

			channelContext.setAttachment(player.getSession());
		} else {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void destroy() {

	}

}
