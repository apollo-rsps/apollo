package org.apollo.cache.map;

import org.apollo.cache.CacheBuffer;
import org.apollo.util.BufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A decoder for reading the map objects for a given map.
 *
 * @author Major
 */
public final class MapLandscapeDecoder {
	/**
	 * Creates a MapObjectsDecoder for the specified map file.
	 *
	 * @param index The map index to decode objects for.
	 * @return The MapObjectsDecoder.
	 * @throws IOException If there is an error reading or decompressing the file.
	 */
	public static MapLandscapeDecoder create(MapIndex index) throws IOException {
		final var folder = index.getLandscapeFolder().get();
		if (folder == null) {
			return null;
		}
		return new MapLandscapeDecoder(folder.findRSFileByID(0).getData());
	}

	/**
	 * The buffer to decode {@link MapObject}s from.
	 */
	private final ByteBuffer buffer;

	/**
	 * Create a new {@link MapLandscapeDecoder} from the given buffer and map coordinates.
	 *
	 * @param buffer The decompressed object file buffer.
	 */
	public MapLandscapeDecoder(CacheBuffer buffer) {
		this.buffer = ByteBuffer.wrap(buffer.getBuffer());
	}

	/**
	 * Decodes the data in the {@code buffer} to a list of {@link MapObject}s.
	 *
	 * @return A list of decoded {@link MapObject}s.
	 */
	public List<MapObject> decode() {
		List<MapObject> objects = new ArrayList<>();

		int id = -1;
		int idOffset = BufferUtil.readHugeSmart(buffer);

		while (idOffset != 0) {
			id += idOffset;

			int packed = 0;
			int positionOffset = BufferUtil.readSmart(buffer);

			while (positionOffset != 0) {
				packed += positionOffset - 1;

				int attributes = buffer.get() & 0xFF;
				int type = attributes >> 2;
				int orientation = attributes & 0x3;
				objects.add(new MapObject(id, packed, type, orientation));

				positionOffset = BufferUtil.readSmart(buffer);
			}

			idOffset = BufferUtil.readHugeSmart(buffer);
		}

		return objects;
	}
}
