package org.apollo.io.player.impl;

import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.security.PlayerCredentials;

/**
 * A {@link PlayerLoader} that utilises {@code JDBC} to load player files.
 * 
 * @author Major
 */
public final class JdbcPlayerLoader implements PlayerLoader {

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		throw new UnsupportedOperationException("JDBC loading is not supported at this time.");
	}

}