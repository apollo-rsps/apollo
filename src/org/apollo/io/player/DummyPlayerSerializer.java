package org.apollo.io.player;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.setting.MembershipStatus;
import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.security.PlayerCredentials;

/**
 * A {@link PlayerSerializer} that saves no data and returns an administrator member account, ideal for debugging.
 * 
 * @author Graham
 * @author Major
 */
public final class DummyPlayerSerializer implements PlayerSerializer {

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) {
		int status = LoginConstants.STATUS_OK;

		Player player = new Player(credentials, TUTORIAL_ISLAND_SPAWN);
		player.setPrivilegeLevel(PrivilegeLevel.ADMINISTRATOR);
		player.setMembers(MembershipStatus.PAID);

		return new PlayerLoaderResponse(status, player);
	}

	@Override
	public void savePlayer(Player player) {
		/* discard player */
	}

}