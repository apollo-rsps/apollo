package org.apollo.io.player.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.io.player.PlayerSaver;

/**
 * A {@link PlayerSaver} that utilises {@code JDBC} to save the player.
 * 
 * @author Major
 */
public final class JdbcPlayerSaver implements PlayerSaver {

	@Override
	public void savePlayer(Player player) throws Exception {
		throw new UnsupportedOperationException("JDBC saving is not supported at this time.");
	}

}