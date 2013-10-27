package org.apollo.io.player.impl;

import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.security.PlayerCredentials;

/**
 * A dummy {@link PlayerLoader} implementation used for testing purposes.
 * @author Graham
 */
public final class DummyPlayerLoader implements PlayerLoader {

	/**
	 * The default spawn position for players loaded by this loader.
	 */
	private static final Position DEFAULT_POSITION = new Position(3222, 3222);

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		int status = LoginConstants.STATUS_OK;

		Player player = new Player(credentials, DEFAULT_POSITION);
		player.setPrivilegeLevel(PrivilegeLevel.ADMINISTRATOR);
		player.setMembers(true);

		return new PlayerLoaderResponse(status, player);
	}

}
