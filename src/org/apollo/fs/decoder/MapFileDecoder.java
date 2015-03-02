package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.fs.archive.ArchiveEntry;

/**
 * Parses {@link MapDefinition map definitions} from the {@link IndexedFileSystem}.
 * 
 * @author Ryley
 */
public final class MapFileDecoder {

	/**
	 * Parses {@link MapDefinition}s from the specified {@link IndexedFileSystem}.
	 *
	 * @param fs The file system.
	 * @return A {@link Map} of parsed map definitions.
	 * @throws IOException If some I/O error occurs.
	 */
	protected static Map<Integer, MapDefinition> parse(IndexedFileSystem fs) throws IOException {
		Archive archive = Archive.decode(fs.getFile(0, 5));
		ArchiveEntry entry = archive.getEntry("map_index");
		ByteBuffer buffer = entry.getBuffer();
		Map<Integer, MapDefinition> defs = new HashMap<>();

		int count = buffer.capacity() / 7;
		for (int i = 0; i < count; i++) {
			int hash = buffer.getShort() & 0xFFFF;
			int terrainFile = buffer.getShort() & 0xFFFF;
			int objectFile = buffer.getShort() & 0xFFFF;
			boolean preload = buffer.get() == 1;

			defs.put(hash, new MapDefinition(hash, terrainFile, objectFile, preload));
		}

		return defs;
	}

	/**
	 * Represents a single map definition.
	 *
	 * @author Ryley
	 */
	public static final class MapDefinition {

		/**
		 * The hash of the region coordinates.
		 */
		private final int hash;

		/**
		 * The terrain file id.
		 */
		private final int terrainFile;

		/**
		 * The object file id.
		 */
		private final int objectFile;

		/**
		 * Whether or not this map is preloaded.
		 */
		private final boolean preload;

		/**
		 * Constructs a new {@link MapDefinition} with the specified hash, terrain file id, object file id and preload
		 * state.
		 *
		 * @param hash The hash of the region coordinates.
		 * @param terrainFile The terrain file id.
		 * @param objectFile The object file id.
		 * @param preload Whether or not this map is preloaded.
		 */
		public MapDefinition(int hash, int terrainFile, int objectFile, boolean preload) {
			this.hash = hash;
			this.terrainFile = terrainFile;
			this.objectFile = objectFile;
			this.preload = preload;
		}

		/**
		 * Returns the coordinate hash.
		 */
		public int getHash() {
			return hash;
		}

		/**
		 * Returns the terrain file id.
		 */
		public int getTerrainFile() {
			return terrainFile;
		}

		/**
		 * Returns the object file id.
		 */
		public int getObjectFile() {
			return objectFile;
		}

		/**
		 * Returns whether or not this map is preloaded.
		 */
		public boolean isPreload() {
			return preload;
		}

	}

}