package org.apollo.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.io.IOException;
import java.util.Optional;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.game.model.entity.Player;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.login.LoginService;
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
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * Creates a login session for the specified channel.
	 *
	 * @param channel The channel.
	 * @param context The server context.
	 */
	public LoginSession(Channel channel, ServerContext context) {
		super(channel);
		this.context = context;
	}

	@Override
	public void destroy() {

	}

	/**
	 * Handles a login request.
	 *
	 * @param request The login request.
	 * @throws IOException If some I/O exception occurs.
	 */
	private void handleLoginRequest(LoginRequest request) throws IOException {
		LoginService loginService = context.getService(LoginService.class);
		loginService.submitLoadRequest(this, request);
	}

	/**
	 * Handles a response from the login service.
	 *
	 * @param request The request this response corresponds to.
	 * @param response The response.
	 */
	public void handlePlayerLoaderResponse(LoginRequest request, PlayerLoaderResponse response) {
		GameService service = context.getService(GameService.class);
		Channel channel = getChannel();

		Optional<Player> optional = response.getPlayer();
		int status = response.getStatus(), rights = 0;
		boolean flagged = false;

		if (optional.isPresent()) {
			Player player = optional.get();
			rights = player.getPrivilegeLevel().toInteger();

			GameSession session = new GameSession(channel, context, player);
			RegistrationStatus registration = service.registerPlayer(player, session);

			if (registration != RegistrationStatus.OK) {
				optional = Optional.empty();
				rights = 0;

				status = registration == RegistrationStatus.ALREADY_ONLINE ? LoginConstants.STATUS_ACCOUNT_ONLINE : LoginConstants.STATUS_SERVER_FULL;
			}
		}

		ChannelFuture future = channel.write(new LoginResponse(status, rights, flagged));

		destroy();

		if (optional.isPresent()) {
			IsaacRandomPair randomPair = request.getRandomPair();
			Release release = context.getRelease();

			channel.pipeline().addFirst("messageEncoder", new GameMessageEncoder(release));
			channel.pipeline().addBefore("messageEncoder", "gameEncoder", new GamePacketEncoder(randomPair.getEncodingRandom()));

			channel.pipeline().addBefore("handler", "gameDecoder", new GamePacketDecoder(randomPair.getDecodingRandom(), context.getRelease()));
			channel.pipeline().addAfter("gameDecoder", "messageDecoder", new GameMessageDecoder(release));

			channel.pipeline().remove("loginDecoder");
			channel.pipeline().remove("loginEncoder");

			channel.attr(NetworkConstants.SESSION_KEY).set(optional.get().getSession());
		} else {
			future.addListener(ChannelFutureListener.CLOSE);
		}

		if (optional.isPresent() && !request.isReconnecting()) {
			optional.get().sendInitialMessages();
		}
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		if (message.getClass() == LoginRequest.class) {
			handleLoginRequest((LoginRequest) message);
		}
	}

}