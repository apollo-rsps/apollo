package org.apollo.io.player;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apollo.util.NameUtil;

/**
 * A utility class with common functionality used by the binary player loader/ savers.
 *
 * @author Graham
 * @author Major
 */
public final class BinaryFileUtils {

	/**
	 * The Path to the saved games directory.
	 */
	private static final Path SAVED_GAMES_DIRECTORY = Paths.get("data/savedGames");

	/**
	 * Creates the saved games directory if it does not exist.
	 */
	static {
		try {
			if (!Files.exists(SAVED_GAMES_DIRECTORY)) {
				Files.createDirectory(SAVED_GAMES_DIRECTORY);
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Error creating saved games directory.", e);
		}
	}

	/**
	 * Gets the save {@link File} for the specified player.
	 *
	 * @param username The username of the player.
	 * @return The file.
	 */
	public static Path getFile(String username) {
		String filtered = NameUtil.decodeBase37(NameUtil.encodeBase37(username));
		return SAVED_GAMES_DIRECTORY.resolve(filtered + ".dat");
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private BinaryFileUtils() {

	}

}