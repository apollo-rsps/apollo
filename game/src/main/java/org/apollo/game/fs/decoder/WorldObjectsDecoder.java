package org.apollo.game.fs.decoder;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.map.MapIndex;
import org.apollo.cache.map.MapObject;
import org.apollo.cache.map.MapObjectsDecoder;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.obj.StaticGameObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

/**
 * A decoder which decodes {@link MapObject}s and registers them with the game world.
 */
public final class WorldObjectsDecoder implements Runnable {
	/**
	 * The IndexedFileSystem.
	 */
	private final IndexedFileSystem fs;

	/**
	 * The {@link RegionRepository} to lookup {@link Region}s from.
	 */
	private final RegionRepository regionRepository;

	/**
	 * The {@link World} to register {@link StaticGameObject}s with.
	 */
	private final World world;

	/**
	 * Create a new {@link WorldObjectsDecoder}.
	 *
	 * @param fs The {@link IndexedFileSystem} to load object files from.
	 * @param world The {@link World} to register objects with.
	 * @param regionRepository The {@link RegionRepository} to lookup {@link Region}s from.
	 */
	public WorldObjectsDecoder(IndexedFileSystem fs, World world, RegionRepository regionRepository) {
		this.fs = fs;
		this.world = world;
		this.regionRepository = regionRepository;
	}

	/**
	 * Decode the {@code MapObject}s from the cache and register them with the world.
	 */
	@Override
	public void run() {
		Map<Integer, MapIndex> mapIndices = MapIndex.getIndices();

		try {
			for (MapIndex index : mapIndices.values()) {
				MapObjectsDecoder decoder = MapObjectsDecoder.create(fs, index);
				List<MapObject> objects = decoder.decode();

				int mapX = index.getX(), mapY = index.getY();

				for (MapObject object : objects) {
					Position position = new Position(mapX + object.getLocalX(), mapY + object.getLocalY(),
						object.getHeight());

					StaticGameObject gameObject = new StaticGameObject(world, object.getId(), position,
						object.getType(), object.getOrientation());

					regionRepository.fromPosition(position).addEntity(gameObject, false);
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
