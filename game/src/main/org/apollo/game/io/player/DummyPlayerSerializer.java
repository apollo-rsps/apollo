package org.apollo.game.io.player;

import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.setting.MembershipStatus;
import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;

/**
 * A {@link PlayerSerializer} that saves no data and returns an administrator member account, ideal for debugging.
 *
 * @author Graham
 * @author Major
 */
public final class DummyPlayerSerializer extends PlayerSerializer {

	/**
	 * Creates the DummyPlayerSerializer.
	 *
	 * @param world The {@link World} to place the {@link Player}s in.
	 */
	public DummyPlayerSerializer(World world) {
		super(world);
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) {
		int status = LoginConstants.STATUS_OK;

		Player player = new Player(world, credentials, TUTORIAL_ISLAND_SPAWN);
		player.setPrivilegeLevel(PrivilegeLevel.ADMINISTRATOR);
		player.setMembers(MembershipStatus.PAID);

		return new PlayerLoaderResponse(status, player);
	}

	@Override
	public void savePlayer(Player player) {
		/* discard player */
	}

}