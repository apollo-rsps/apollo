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
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorRepository;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.GameObject;
import org.apollo.util.BufferUtil;
import org.apollo.util.CompressionUtil;

import com.google.common.collect.Iterables;

/**
 * Parses static object definitions, which include map tiles and landscapes.
 * 
 * @author Ryley
 */
public final class GameObjectDecoder {

	/**
	 * A bit flag which denotes that a specified Position is blocked.
	 */
	private static final int FLAG_BLOCKED = 1;

	/**
	 * A bit flag which denotes that a specified Position is a bridge.
	 */
	private static final int FLAG_BRIDGE = 2;

	/**
	 * The sector repository.
	 */
	private static final SectorRepository REPOSITORY = World.getWorld().getSectorRepository();

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

	/**
	 * A {@link List} of decoded game objects.
	 */
	private final List<GameObject> objects = new ArrayList<>();

	/**
	 * Creates the decoder.
	 * 
	 * @param fs The indexed file system.
	 */
	public GameObjectDecoder(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Decodes all static objects and places them in the returned array.
	 * 
	 * @return The decoded objects.
	 * @throws IOException If an I/O error occurs.
	 */
	public GameObject[] decode() throws IOException {
		Map<Integer, MapDefinition> definitions = MapFileDecoder.decode(fs);

		for (Entry<Integer, MapDefinition> entry : definitions.entrySet()) {
			MapDefinition def = entry.getValue();

			int packed = def.getPacketCoordinates();
			int x = (packed >> 8 & 0xFF) * 64;
			int y = (packed & 0xFF) * 64;

			ByteBuffer gameObjectData = fs.getFile(4, def.getObjectFile());
			ByteBuffer gameObjectBuffer = ByteBuffer.wrap(CompressionUtil.degzip(gameObjectData));
			parseGameObject(gameObjectBuffer, x, y);

			ByteBuffer terrainData = fs.getFile(4, def.getTerrainFile());
			ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.degzip(terrainData));
			parseTerrain(terrainBuffer, x, y);
		}

		return Iterables.toArray(objects, GameObject.class);
	}

	private void parseGameObject(ByteBuffer buffer, int x, int y) {
		for (int deltaId, id = -1; (deltaId = BufferUtil.readSmart(buffer)) != 0;) {
			id += deltaId;

			for (int deltaPos, pos = 0; (deltaPos = BufferUtil.readSmart(buffer)) != 0;) {
				pos += deltaPos - 1;

				int localY = pos & 0x3F;
				int localX = pos >> 6 & 0x3F;
				int height = pos >> 12;

				int attributes = buffer.get() & 0xFF;
				int type = attributes >> 2;
				int orientation = attributes & 0x3;
				Position position = new Position(x + localX, y + localY, height);

				gameObjectDecoded(id, orientation, type, position);
			}
		}
	}

	private void gameObjectDecoded(int id, int orientation, int type, Position position) {
		ObjectDefinition definition = ObjectDefinition.lookup(id);

		Sector sector = REPOSITORY.fromPosition(position);
		int x = position.getX(), y = position.getY(), height = position.getHeight();

		if (height < 0)
			return;

		CollisionMatrix matrix = sector.getMatrix(height);

		boolean block = false;

		// Ground decoration, signs, water fountains, etc
		if (type == 22 && definition.isInteractive()) {
			block = true;
		}

		Predicate<Integer> walls = (value) -> value >= 0 && value < 4 || value == 9;
		Predicate<Integer> roofs = (value) -> value >= 12 && value < 22;

		// Walls and roofs that intercept may intercept a mob when moving
		if (walls.test(type) || roofs.test(type)) {
			block = true;
		}

		// General objects, trees, statues, etc
		if (type == 10 && definition.isSolid()) {
			block = true;
		}

		if (block) {
			for (int width = 0; width < definition.getWidth(); width++) {
				for (int length = 0; length < definition.getLength(); length++) {
					int localX = (x % Sector.SECTOR_SIZE) + width, localY = (y % Sector.SECTOR_SIZE) + length;

					if (localX > 7 || localY > 7) {
						Sector next = REPOSITORY.fromPosition(new Position(x + localX - 7, y + localY - 7));
						next.getMatrix(height).block(localX, localY);
						continue;
					}

					matrix.block(localX, localY);
				}
			}
		}

		objects.add(new GameObject(id, position, type, orientation));
	}

	private void parseTerrain(ByteBuffer buffer, int x, int y) {
		for (int height = 0; height < 4; height++) {
			for (int localX = 0; localX < 64; localX++) {
				for (int localY = 0; localY < 64; localY++) {
					Position position = new Position(x + localX, y + localY, height);

					int flags = 0;
					while (true) {
						int attributeId = buffer.get() & 0xFF;
						if (attributeId == 0) {
							terrainDecoded(flags, position);
							break;
						} else if (attributeId == 1) {
							buffer.get();
							terrainDecoded(flags, position);
							break;
						} else if (attributeId <= 49) {
							buffer.get();
						} else if (attributeId <= 81) {
							flags = attributeId - 49;
						}
					}
				}
			}
		}
	}

	private void terrainDecoded(int flags, Position position) {
		Sector sector = REPOSITORY.fromPosition(position);
		int x = position.getX(), y = position.getY(), height = position.getHeight();

		if (height < 0)
			return;
		CollisionMatrix current = sector.getMatrix(height);

		boolean block = false;
		if ((flags & FLAG_BLOCKED) != 0) {
			block = true;
		}

		if ((flags & FLAG_BRIDGE) != 0) {
			if (--height >= 0) {
				block = true;
			}
		}

		if (block) {
			int localX = (x % Sector.SECTOR_SIZE), localY = (y % Sector.SECTOR_SIZE);

			if (localX > 7 || localY > 7) {
				Sector next = REPOSITORY.fromPosition(new Position(x + localX - 7, y + localY - 7));
				next.getMatrix(height).block(localX, localY);
				return;
			}

			current.block(localX, localY);
		}
	}

}