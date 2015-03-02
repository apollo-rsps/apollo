package org.apollo.io.player.impl;

import java.io.File;

import org.apollo.util.NameUtil;

/**
 * A utility class with common functionality used by the binary player loader/ savers.
 * 
 * @author Graham
 */
public final class BinaryPlayerUtil {

	/**
	 * The saved games directory.
	 */
	private static final File SAVED_GAMES_DIRECTORY = new File("data/savedGames");

	/**
	 * Creates the saved games directory if it does not exist.
	 */
	static {
		if (!SAVED_GAMES_DIRECTORY.exists()) {
			SAVED_GAMES_DIRECTORY.mkdir();
		}
	}

	/**
	 * Gets the save {@link File} for the specified player.
	 * 
	 * @param username The username of the player.
	 * @return The file.
	 */
	public static File getFile(String username) {
		String filtered = NameUtil.decodeBase37(NameUtil.encodeBase37(username));
		return new File(SAVED_GAMES_DIRECTORY, filtered + ".dat");
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private BinaryPlayerUtil() {

	}

}