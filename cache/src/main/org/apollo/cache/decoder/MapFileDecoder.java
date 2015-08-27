package org.apollo.cache.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.archive.Archive;
import org.apollo.cache.archive.ArchiveEntry;

/**
 * Decodes {@link MapDefinition}s from the {@link IndexedFileSystem}.
 *
 * @author Ryley
 * @author Major
 */
public final class MapFileDecoder {

	/**
	 * A definition for a region.
	 */
	public static final class MapDefinition {

		/**
		 * Indicates whether or not this map is members-only.
		 */
		private final boolean members;

		/**
		 * The object file id.
		 */
		private final int objects;

		/**
		 * The packed coordinates.
		 */
		private final int packedCoordinates;

		/**
		 * The terrain file id.
		 */
		private final int terrain;

		/**
		 * Creates the {@link MapDefinition}.
		 *
		 * @param packedCoordinates The packed coordinates.
		 * @param terrain The terrain file id.
		 * @param objects The object file id.
		 * @param members Indicates whether or not this map is members-only.
		 */
		public MapDefinition(int packedCoordinates, int terrain, int objects, boolean members) {
			this.packedCoordinates = packedCoordinates;
			this.terrain = terrain;
			this.objects = objects;
			this.members = members;
		}

		/**
		 * Gets the id of the file containing the object data.
		 *
		 * @return The file id.
		 */
		public int getObjectFile() {
			return objects;
		}

		/**
		 * Gets the packed coordinates.
		 *
		 * @return The packed coordinates.
		 */
		public int getPackedCoordinates() {
			return packedCoordinates;
		}

		/**
		 * Gets the id of the file containing the terrain data.
		 *
		 * @return The file id.
		 */
		public int getTerrainFile() {
			return terrain;
		}

		/**
		 * Returns whether or not this MapDefinition is for a members-only area of the world.
		 *
		 * @return {@code true} if this MapDefinition is for a members-only area, {@code false} if not.
		 */
		public boolean isMembersOnly() {
			return members;
		}

	}

	/**
	 * The width (and length) of a map file, in tiles.
	 */
	public static final int MAP_FILE_WIDTH = 64;

	/**
	 * The file id of the versions archive.
	 */
	private static final int VERSIONS_ARCHIVE_FILE_ID = 5;

	/**
	 * Decodes {@link MapDefinition}s from the specified {@link IndexedFileSystem}.
	 *
	 * @param fs The IndexedFileSystem.
	 * @return A {@link Map} of packed coordinates to their MapDefinitions.
	 * @throws IOException If there is an error reading or decoding the Archive.
	 */
	public static Map<Integer, MapDefinition> decode(IndexedFileSystem fs) throws IOException {
		Archive archive = fs.getArchive(0, VERSIONS_ARCHIVE_FILE_ID);
		ArchiveEntry entry = archive.getEntry("map_index");
		Map<Integer, MapDefinition> definitions = new HashMap<>();

		ByteBuffer buffer = entry.getBuffer();
		int count = buffer.capacity() / (3 * Short.BYTES + Byte.BYTES);

		for (int times = 0; times < count; times++) {
			int packed = buffer.getShort() & 0xFFFF;
			int terrain = buffer.getShort() & 0xFFFF;
			int objects = buffer.getShort() & 0xFFFF;
			boolean members = buffer.get() == 1;

			definitions.put(packed, new MapDefinition(packed, terrain, objects, members));
		}

		return definitions;
	}

}