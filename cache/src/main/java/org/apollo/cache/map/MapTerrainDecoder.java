package org.apollo.cache.map;

import org.apollo.cache.CacheBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A decoder for the terrain data stored in {@link MapFile}s.
 *
 * @author Major
 */
public class MapTerrainDecoder {
	/**
	 * Creates a MapFileDecoder for the specified map file.
	 *
	 * @param index The {@link MapIndex} to get the file index from.
	 * @return The MapFileDecoder.
	 * @throws IOException If there is an error reading or decompressing the file.
	 */
	public static MapTerrainDecoder create(MapIndex index) throws IOException {
		final var folder = index.getTerrainFolder().get();
		if (folder == null) {
			return null;
		}
		return new MapTerrainDecoder(folder.findRSFileByID(0).getData());
	}

	/**
	 * The DataBuffer containing the MapFile data.
	 */
	private final ByteBuffer buffer;

	/**
	 * Creates the MapIndexDecoder.
	 * <p>
	 * This constructor expects the {@link ByteBuffer} to <strong>not</strong> be compressed.
	 *
	 * @param buffer The DataBuffer containing the MapFile data.
	 */
	public MapTerrainDecoder(CacheBuffer buffer) {
		this.buffer = ByteBuffer.wrap(buffer.getBuffer());
	}

	/**
	 * Decodes the data into a {@link MapFile}.
	 *
	 * @return The MapFile.
	 */
	public MapFile decode() {
		MapPlane[] planes = new MapPlane[MapConstants.MAP_PLANES];

		for (int level = 0; level < MapConstants.MAP_PLANES; level++) {
			planes[level] = decodePlane(planes, level);
		}

		return new MapFile(planes);
	}

	/**
	 * Decodes a {@link MapPlane} with the specified level.
	 *
	 * @param planes The previously-decoded {@link MapPlane}s, for calculating the height of the tiles.
	 * @param level  The level.
	 * @return The MapPlane.
	 */
	private MapPlane decodePlane(MapPlane[] planes, int level) {
		Tile[][] tiles = new Tile[MapConstants.MAP_WIDTH][MapConstants.MAP_WIDTH];

		for (int x = 0; x < MapConstants.MAP_WIDTH; x++) {
			for (int z = 0; z < MapConstants.MAP_WIDTH; z++) {
				tiles[x][z] = decodeTile(planes, level, x, z);
			}
		}

		return new MapPlane(level, tiles);
	}

	/**
	 * Decodes the data into a {@link Tile}.
	 *
	 * @param planes The previously-decoded {@link MapPlane}s, for calculating the height of the Tile.
	 * @param level  The level the Tile is on.
	 * @param x      The x coordinate of the Tile.
	 * @param z      The z coordinate of the Tile.
	 * @return The MapFile.
	 */
	private Tile decodeTile(MapPlane[] planes, int level, int x, int z) {
		Tile.Builder builder = Tile.builder(x, z, level);

		int type;
		do {
			type = buffer.get() & 0xFF;

			if (type == 0) {
				if (level == 0) {
					builder.setHeight(TileUtils.calculateHeight(x, z));
				} else {
					Tile below = planes[level - 1].getTile(x, z);
					builder.setHeight(below.getHeight() + MapConstants.PLANE_HEIGHT_DIFFERENCE);
				}
			} else if (type == 1) {
				int height = buffer.get();
				int below = (level == 0) ? 0 : planes[level - 1].getTile(x, z).getHeight();

				builder.setHeight((height == 1 ? 0 : height) * MapConstants.HEIGHT_MULTIPLICAND + below);
			} else if (type <= MapConstants.MINIMUM_OVERLAY_TYPE) {
				builder.setOverlay(buffer.get());
				builder.setOverlayType((type - MapConstants.LOWEST_CONTINUED_TYPE) / MapConstants.ORIENTATION_COUNT);
				builder.setOverlayOrientation(
						type - MapConstants.LOWEST_CONTINUED_TYPE % MapConstants.ORIENTATION_COUNT);
			} else if (type <= MapConstants.MINIMUM_ATTRIBUTES_TYPE) {
				builder.setAttributes(type - MapConstants.MINIMUM_OVERLAY_TYPE);
			} else {
				builder.setUnderlay(type - MapConstants.MINIMUM_ATTRIBUTES_TYPE);
			}
		} while (type >= MapConstants.LOWEST_CONTINUED_TYPE);

		return builder.build();
	}
}
