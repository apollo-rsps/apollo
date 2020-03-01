package org.apollo.cache.map;

import org.apollo.cache.Cache;
import org.apollo.cache.Archive;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes {@link MapIndex}s from the {@link Archive}.
 *
 * @author Ryley
 * @author Major
 */
public final class MapIndexDecoder implements Runnable {

	/**
	 * The file id of the versions archive.
	 */
	private static final int VERSIONS_ARCHIVE_FILE_ID = 5;

	/**
	 * The Cache.
	 */
	private final Cache cache;
	private final XteaParser xteaParser;

	public MapIndexDecoder(Cache cache, XteaParser xteaParser) {
		this.cache = cache;
		this.xteaParser = xteaParser;
	}

	/**
	 * Decodes {@link MapIndex}s from the specified {@link Archive}.
	 *
	 * @return A {@link Map} of packed coordinates to their MapDefinitions.
	 * @throws IOException If there is an error reading or decoding the Archive.
	 */
	public Map<Integer, MapIndex> decode() throws IOException {
		Map<Integer, MapIndex> definitions = new HashMap<>();

		final var fs = cache.getArchive(MapConstants.MAP_INDEX);
		for (var entry : xteaParser.getAll()) {
			final var region = entry.getIntKey();
			final var regionX = region >> 8;
			final var regionY = region & 0xFF;

			try {
				final var terrainFolder = fs.findFolderByName("m" + regionX + "_" + regionY);
				final var landscapeFolder = fs.findFolderByName("l" + regionX + "_" + regionY, entry.getValue());
				if (landscapeFolder == null) {
					continue;
				}

				definitions.put(region, new MapIndex(region, terrainFolder, landscapeFolder, true));
			} catch (Throwable e) {

			}
		}

		return definitions;
	}

	@Override
	public void run() {
		try {
			xteaParser.run();
			MapIndex.init(decode());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}