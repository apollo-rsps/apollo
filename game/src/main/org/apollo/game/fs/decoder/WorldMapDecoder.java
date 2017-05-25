package org.apollo.game.fs.decoder;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.map.MapConstants;
import org.apollo.cache.map.MapFile;
import org.apollo.cache.map.MapFileDecoder;
import org.apollo.cache.map.MapIndex;
import org.apollo.cache.map.MapPlane;
import org.apollo.cache.map.Tile;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionManager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * A decoder which loads {@link MapFile}s and notifies the {@link CollisionManager} of tiles which are blocked,
 * or on a bridge.
 */
public final class WorldMapDecoder implements Runnable {

	/**
	 * A bit flag that indicates that the tile at the current Position is blocked.
	 */
	private static final int BLOCKED_TILE = 0x1;

	/**
	 * A bit flag that indicates that the tile at the current Position is a bridge tile.
	 */
	private static final int BRIDGE_TILE = 0x2;

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private IndexedFileSystem fs;

	/**
	 * The {@link CollisionManager} to notify of bridged / blocked tiles.
	 */
	private CollisionManager collisionManager;

	/**
	 * Create a new {@link WorldMapDecoder}.
	 *
	 * @param fs The {@link IndexedFileSystem} to load {@link MapFile}s. from.
	 * @param collisionManager The {@link CollisionManager} to register tiles with.
	 */
	public WorldMapDecoder(IndexedFileSystem fs, CollisionManager collisionManager) {
		this.fs = fs;
		this.collisionManager = collisionManager;
	}

	/**
	 * Decode all {@link MapFile}s and notify the {@link CollisionManager} of any tiles that are
	 * flagged as blocked or on a bridge.
	 */
	@Override
	public void run() {
		Map<Integer, MapIndex> mapIndices = MapIndex.getIndices();

		try {
			for (MapIndex index : mapIndices.values()) {
				MapFileDecoder decoder = MapFileDecoder.create(fs, index);
				MapFile mapFile = decoder.decode();
				MapPlane[] mapPlanes = mapFile.getPlanes();

				int mapX = index.getX(), mapY = index.getY();
				for (MapPlane plane : mapPlanes) {
					markTiles(mapX, mapY, plane);
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * Mark any tiles in the given {@link MapPlane} as blocked or bridged in the {@link CollisionManager}.
	 *
	 * @param mapX The X coordinate of the map file.
	 * @param mapY The Y coordinate of the map file.
	 * @param plane The {@link MapPlane} to load tiles from.
	 */
	private void markTiles(int mapX, int mapY, MapPlane plane) {
		for (int x = 0; x < MapConstants.MAP_WIDTH; x++) {
			for (int y = 0; y < MapConstants.MAP_WIDTH; y++) {
				Tile tile = plane.getTile(x, y);
				Position position = new Position(mapX + x, mapY + y, plane.getLevel());

				if ((tile.getAttributes() & BLOCKED_TILE) == BLOCKED_TILE) {
					collisionManager.block(position);
				}

				if ((tile.getAttributes() & BRIDGE_TILE) == BRIDGE_TILE) {
					collisionManager.markBridged(position);
				}
			}
		}
	}
}
