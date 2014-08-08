package org.apollo.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.game.model.entity.Player;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.login.LoginService;
import org.apollo.net.ApolloHandler;
import org.apollo.net.NetworkConstants;
import org.apollo.net.codec.game.GameMessageDecoder;
import org.apollo.net.codec.game.GameMessageEncoder;
import org.apollo.net.codec.game.GamePacketDecoder;
import org.apollo.net.codec.game.GamePacketEncoder;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.codec.login.LoginResponse;
import org.apollo.net.release.Release;
import org.apollo.security.IsaacRandomPair;

/**
 * A login session.
 * 
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
	 * 
	 * @param ctx The context of the {@link ApolloHandler}.
	 * @param serverContext The server context.
	 */
	public LoginSession(ChannelHandlerContext ctx, ServerContext serverContext) {
		super(ctx.channel());
		this.channelContext = ctx;
		this.serverContext = serverContext;
	}

	@Override
	public void destroy() {

	}

	/**
	 * Gets the release.
	 * 
	 * @return The release.
	 */
	public Release getRelease() {
		return serverContext.getRelease();
	}

	/**
	 * Handles a login request.
	 * 
	 * @param request The login request.
	 */
	private void handleLoginRequest(LoginRequest request) {
		LoginService loginService = serverContext.getService(LoginService.class);
		loginService.submitLoadRequest(this, request);
	}

	/**
	 * Handles a response from the login service.
	 * 
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
				rights = 0;
				if (registrationStatus == RegistrationStatus.ALREADY_ONLINE) {
					status = LoginConstants.STATUS_ACCOUNT_ONLINE;
				} else {
					status = LoginConstants.STATUS_SERVER_FULL;
				}
			}
		}

		ChannelFuture future = channel.writeAndFlush(new LoginResponse(status, rights, log));
		destroy();

		if (player != null) {
			IsaacRandomPair randomPair = request.getRandomPair();
			Release release = serverContext.getRelease();

			channel.pipeline().addFirst("eventEncoder", new GameMessageEncoder(release));
			channel.pipeline().addBefore("eventEncoder", "gameEncoder",
					new GamePacketEncoder(randomPair.getEncodingRandom()));

			channel.pipeline().addBefore("handler", "gameDecoder",
					new GamePacketDecoder(randomPair.getDecodingRandom(), serverContext.getRelease()));
			channel.pipeline().addAfter("gameDecoder", "eventDecoder", new GameMessageDecoder(release));

			channel.pipeline().remove("loginDecoder");
			channel.pipeline().remove("loginEncoder");

			channelContext.attr(NetworkConstants.SESSION_KEY).set(player.getSession());
		} else {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void messageReceived(Object message) {
		if (message.getClass() == LoginRequest.class) {
			handleLoginRequest((LoginRequest) message);
		}
	}

}