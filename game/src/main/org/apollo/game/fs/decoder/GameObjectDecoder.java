package org.apollo.game.fs.decoder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.decoder.MapFileDecoder;
import org.apollo.cache.decoder.MapFileDecoder.MapDefinition;
import org.apollo.cache.decoder.ObjectDefinitionDecoder;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.io.player.PlayerSerializer;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.game.model.entity.obj.ObjectType;
import org.apollo.game.model.entity.obj.StaticGameObject;
import org.apollo.util.BufferUtil;
import org.apollo.util.CompressionUtil;

/**
 * Parses static object definitions, which include map tiles and landscapes.
 *
 * @author Ryley
 * @author Major
 */
public final class GameObjectDecoder implements Runnable {

	/**
	 * A bit flag that indicates that the tile at the current Position is blocked.
	 */
	private static final int BLOCKED_TILE = 1;

	/**
	 * A bit flag that indicates that the tile at the current Position is a bridge tile.
	 */
	private static final int BRIDGE_TILE = 2;

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

	/**
	 * A {@link List} of decoded GameObjects.
	 */
	private final List<GameObject> objects = new ArrayList<>();

	/**
	 * The RegionRepository.
	 */
	private final RegionRepository regions;

	/**
	 * The World to place the objects in.
	 */
	private final World world;

	/**
	 * The most-recently used Region.
	 */
	private Region previous;

	/**
	 * Creates the GameObjectDecoder.
	 *
	 * @param fs The {@link IndexedFileSystem}.
	 * @param world The {@link World} to place the objects in.
	 */
	public GameObjectDecoder(IndexedFileSystem fs, World world) {
		this.fs = fs;
		this.world = world;
		regions = world.getRegionRepository();
		previous = regions.fromPosition(PlayerSerializer.TUTORIAL_ISLAND_SPAWN); // dummy, so 'previous' is never null.
	}

	@Override
	public void run() {
		ObjectDefinitionDecoder decoder = new ObjectDefinitionDecoder(fs);
		decoder.run();

		try {
			Map<Integer, MapDefinition> definitions = MapFileDecoder.decode(fs);

			for (MapDefinition definition : definitions.values()) {
				int packed = definition.getPackedCoordinates();
				int x = (packed >> 8 & 0xFF) * (Region.SIZE * Region.SIZE);
				int y = (packed & 0xFF) * (Region.SIZE * Region.SIZE);

				ByteBuffer objects = fs.getFile(4, definition.getObjectFile());
				ByteBuffer decompressed = ByteBuffer.wrap(CompressionUtil.degzip(objects));
				decodeObjects(decompressed, x, y);

				ByteBuffer terrain = fs.getFile(4, definition.getTerrainFile());
				decompressed = ByteBuffer.wrap(CompressionUtil.degzip(terrain));
				decodeTerrain(decompressed, x, y);
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Error decoding StaticGameObjects.", e);
		}

		objects.forEach(object -> regions.fromPosition(object.getPosition()).addEntity(object, false));
	}

	/**
	 * Blocks tiles covered by a GameObject, if applicable.
	 *
	 * @param object The {@link GameObject}.
	 * @param position The position of the GameObject.
	 */
	private void block(GameObject object, Position position) {
		ObjectDefinition definition = ObjectDefinition.lookup(object.getId());
		int type = object.getType();

		int x = position.getX(), y = position.getY(), height = position.getHeight();

		if (!previous.contains(position)) {
			previous = regions.fromPosition(position);
		}

		CollisionMatrix matrix = previous.getMatrix(height);
		if (unwalkable(definition, type)) {
			int width = definition.getWidth(), length = definition.getLength();

			for (int dx = 0; dx < width; dx++) {
				for (int dy = 0; dy < length; dy++) {
					int localX = x % Region.SIZE + dx, localY = y % Region.SIZE + dy;

					if (localX > 7 || localY > 7) {
						int nextLocalX = localX > 7 ? x + localX - 7 : x + localX;
						int nextLocalY = localY > 7 ? y + localY - 7 : y - localY;
						Region next = regions.fromPosition(new Position(nextLocalX, nextLocalY));

						int nextX = nextLocalX % Region.SIZE + dx;
						int nextY = nextLocalY % Region.SIZE + dy;

						if (nextX > 7) {
							nextX -= 7;
						}

						if (nextY > 7) {
							nextY -= 7;
						}

						next.getMatrix(height).block(nextX, nextY);
						continue;
					}

					matrix.block(localX, localY);
				}
			}
		}
	}

	/**
	 * Decodes the attributes of a terrain file, blocking the tile if necessary.
	 *
	 * @param attributes The terrain attributes.
	 * @param x The x coordinate of the tile the attributes belong to.
	 * @param y The y coordinate of the tile the attributes belong to.
	 * @param height The level level of the tile the attributes belong to.
	 */
	private void decodeAttributes(int attributes, int x, int y, int height) {
		boolean block = false;
		if ((attributes & BLOCKED_TILE) != 0) {
			block = true;
		}

		if ((attributes & BRIDGE_TILE) != 0 && height >0) {
				block = true;
				height--;
		}

		if (block) {
			int localX = x % Region.SIZE, localY = y % Region.SIZE;
			Position position = new Position(x, y, height);

			if (!previous.contains(position)) {
				previous = regions.fromPosition(position);
			}

			previous.getMatrix(height).block(localX, localY);
		}
	}

	/**
	 * Decodes object data stored in the specified {@link ByteBuffer}.
	 *
	 * @param buffer The ByteBuffer to decode data from.
	 * @param x The x coordinate of the top left tile of the map file.
	 * @param y The y coordinate of the top left tile of the map file.
	 */
	private void decodeObjects(ByteBuffer buffer, int x, int y) {
		int id = -1;
		int idOffset = BufferUtil.readSmart(buffer);

		while (idOffset != 0) {
			id += idOffset;

			int packed = 0;
			int positionOffset = BufferUtil.readSmart(buffer);

			while (positionOffset != 0) {
				packed += positionOffset - 1;

				int localY = packed & 0x3F;
				int localX = packed >> 6 & 0x3F;
				int height = packed >> 12 & 0x3;

				int attributes = buffer.get() & 0xFF;
				int type = attributes >> 2;
				int orientation = attributes & 0x3;
				Position position = new Position(x + localX, y + localY, height);

				GameObject object = new StaticGameObject(world, id, position, type, orientation);
				objects.add(object);

				block(object, position);
				positionOffset = BufferUtil.readSmart(buffer);
			}

			idOffset = BufferUtil.readSmart(buffer);
		}
	}

	/**
	 * Decodes terrain data stored in the specified {@link ByteBuffer}.
	 *
	 * @param buffer The ByteBuffer.
	 * @param x The x coordinate of the top left tile of the map file.
	 * @param y The y coordinate of the top left tile of the map file.
	 */
	private void decodeTerrain(ByteBuffer buffer, int x, int y) {
		for (int height = 0; height < Position.HEIGHT_LEVELS; height++) {
			for (int localX = 0; localX < Region.SIZE * Region.SIZE; localX++) {
				for (int localY = 0; localY < Region.SIZE * Region.SIZE; localY++) {
					int attributes = 0;

					while (true) {
						int attributeId = buffer.get() & 0xFF;

						if (attributeId == 0) {
							decodeAttributes(attributes, x + localX, y + localY, height);
							break;
						} else if (attributeId == 1) {
							buffer.get();
							decodeAttributes(attributes, x + localX, y + localY, height);
							break;
						} else if (attributeId <= 49) {
							buffer.get();
						} else if (attributeId <= 81) {
							attributes = attributeId - 49;
						}
					}
				}
			}
		}
	}

	/**
	 * Returns whether or not an object with the specified {@link ObjectDefinition} and {@code type} should result in
	 * the tile(s) it is located on being blocked.
	 *
	 * @param definition The {@link ObjectDefinition} of the object.
	 * @param type The type of the object.
	 * @return {@code true} iff the tile(s) the object is on should be blocked.
	 */
	private boolean unwalkable(ObjectDefinition definition, int type) {
		// TODO figure out the other ObjectTypes and get rid of all the getValue() calls
		return (type == ObjectType.FLOOR_DECORATION.getValue() && definition.isInteractive()) ||
			(type >= ObjectType.LENGTHWISE_WALL.getValue() && type <= ObjectType.RECTANGULAR_CORNER.getValue()) ||
			(type > ObjectType.DIAGONAL_INTERACTABLE.getValue() && type < ObjectType.FLOOR_DECORATION.getValue()) ||
			(type == ObjectType.INTERACTABLE.getValue() && definition.isSolid()) ||
			type == ObjectType.DIAGONAL_WALL.getValue();
	}

}