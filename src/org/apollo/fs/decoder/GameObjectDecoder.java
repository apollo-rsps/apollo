package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.decoder.MapFileDecoder.MapDefinition;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.game.model.entity.obj.ObjectType;
import org.apollo.game.model.entity.obj.StaticGameObject;
import org.apollo.util.BufferUtil;
import org.apollo.util.CompressionUtil;

import com.google.common.collect.Iterables;

/**
 * Parses static object definitions, which include map tiles and landscapes.
 * 
 * @author Ryley
 * @author Major
 */
public final class GameObjectDecoder {

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
	 * Creates the GameObjectDecoder.
	 * 
	 * @param fs The {@link IndexedFileSystem}.
	 * @param regions The {@link RegionRepository}.
	 */
	public GameObjectDecoder(IndexedFileSystem fs, RegionRepository regions) {
		this.fs = fs;
		this.regions = regions;
	}

	/**
	 * Decodes the GameObjects from their MapDefinitions.
	 * 
	 * @return The decoded objects.
	 * @throws IOException If there is an error decoding the {@link MapDefinition}s.
	 */
	public GameObject[] decode() throws IOException {
		Map<Integer, MapDefinition> definitions = MapFileDecoder.decode(fs);

		for (Entry<Integer, MapDefinition> entry : definitions.entrySet()) {
			MapDefinition definition = entry.getValue();

			int packed = definition.getPackedCoordinates();
			int x = (packed >> 8 & 0xFF) * 64;
			int y = (packed & 0xFF) * 64;

			ByteBuffer objects = fs.getFile(4, definition.getObjectFile());
			ByteBuffer decompressed = ByteBuffer.wrap(CompressionUtil.degzip(objects));
			decodeObjects(decompressed, x, y);

			ByteBuffer terrain = fs.getFile(4, definition.getTerrainFile());
			decompressed = ByteBuffer.wrap(CompressionUtil.degzip(terrain));
			decodeTerrain(decompressed, x, y);
		}

		return Iterables.toArray(objects, GameObject.class);
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

		Region region = regions.fromPosition(position);
		int x = position.getX(), y = position.getY(), height = position.getHeight();

		CollisionMatrix matrix = region.getMatrix(height);

		boolean block = false;

		if (type == ObjectType.FLOOR_DECORATION.getValue() && definition.isInteractive()) {
			block = true;
		}

		Predicate<Integer> walls = (value) -> value >= ObjectType.LENGTHWISE_WALL.getValue()
				&& value <= ObjectType.RECTANGULAR_CORNER.getValue() || value == ObjectType.DIAGONAL_WALL.getValue();

		Predicate<Integer> roofs = (value) -> value > ObjectType.DIAGONAL_INTERACTABLE.getValue()
				&& value < ObjectType.FLOOR_DECORATION.getValue();

		if (walls.test(type) || roofs.test(type)) {
			block = true;
		}

		if (type == 10 && definition.isSolid()) {
			block = true;
		}

		if (block) {
			for (int dx = 0; dx < definition.getWidth(); dx++) {
				for (int dy = 0; dy < definition.getLength(); dy++) {
					int localX = (x % Region.SIZE) + dx, localY = (y % Region.SIZE) + dy;

					if (localX > 7 || localY > 7) {
						int nextLocalX = localX > 7 ? x + localX - 7 : x + localX;
						int nextLocalY = localY > 7 ? y + localY - 7 : y - localY;
						Position nextPosition = new Position(nextLocalX, nextLocalY);
						Region next = regions.fromPosition(nextPosition);

						int nextX = (nextPosition.getX() % Region.SIZE) + dx, nextY = (nextPosition.getY() % Region.SIZE) + dy;
						if (nextX > 7)
							nextX -= 7;
						if (nextY > 7)
							nextY -= 7;

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
	 * @param position The {@link Position} of the tile whose attributes are being decoded.
	 */
	private void decodeAttributes(int attributes, Position position) {
		Region region = regions.fromPosition(position);
		int x = position.getX(), y = position.getY(), height = position.getHeight();

		CollisionMatrix current = region.getMatrix(height);

		boolean block = false;
		if ((attributes & BLOCKED_TILE) != 0) {
			block = true;
		}
		if ((attributes & BRIDGE_TILE) != 0) {
			if (height > 0) {
				block = true;
				height--;
			}
		}

		if (block) {
			int localX = (x % Region.SIZE), localY = (y % Region.SIZE);
			current.block(localX, localY);
		}
	}

	/**
	 * Decodes object data stored in the specified {@link ByteBuffer}.
	 * 
	 * @param buffer The ByteBuffer.
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
				int height = (packed >> 12) & 0x3;

				int attributes = buffer.get() & 0xFF;
				int type = attributes >> 2;
				int orientation = attributes & 0x3;
				Position position = new Position(x + localX, y + localY, height);

				GameObject object = new StaticGameObject(id, position, type, orientation);
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
		for (int height = 0; height < 4; height++) {
			for (int localX = 0; localX < 64; localX++) {
				for (int localY = 0; localY < 64; localY++) {
					Position position = new Position(x + localX, y + localY, height);

					int attributes = 0;
					while (true) {
						int attributeId = buffer.get() & 0xFF;
						if (attributeId == 0) {
							decodeAttributes(attributes, position);
							break;
						} else if (attributeId == 1) {
							buffer.get();
							decodeAttributes(attributes, position);
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

}