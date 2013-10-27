package org.apollo.io.player.impl;

import java.io.File;

import org.apollo.util.NameUtil;

/**
 * A utility class with common functionality used by the binary player loader/
 * savers.
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
	 * Gets the file for the specified player.
	 * @param name The name of the player.
	 * @return The file.
	 */
	public static File getFile(String name) {
		name = NameUtil.decodeBase37(NameUtil.encodeBase37(name));
		return new File(SAVED_GAMES_DIRECTORY, name + ".dat");
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private BinaryPlayerUtil() {

	}

}
