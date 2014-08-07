package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.GameObject;
import org.apollo.util.BufferUtil;
import org.apollo.util.CompressionUtil;

/**
 * Decodes map object data from the {@code map_index.dat} file into {@link GameObject}s.
 * 
 * @author Chris Fletcher
 */
public final class GameObjectDecoder {

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

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
		Archive versionList = Archive.decode(fs.getFile(0, 5));
		ByteBuffer buffer = versionList.getEntry("map_index").getBuffer();

		int indices = buffer.remaining() / 7;
		int[] areas = new int[indices];
		int[] landscapes = new int[indices];

		for (int i = 0; i < indices; i++) {
			areas[i] = buffer.getShort() & 0xFFFF;

			@SuppressWarnings("unused")
			int mapFile = buffer.getShort() & 0xFFFF;

			landscapes[i] = buffer.getShort() & 0xFFFF;

			@SuppressWarnings("unused")
			boolean members = (buffer.get() & 0xFF) == 1;
		}

		List<GameObject> objects = new ArrayList<>();

		for (int i = 0; i < indices; i++) {
			ByteBuffer compressed = fs.getFile(4, landscapes[i]);
			ByteBuffer uncompressed = ByteBuffer.wrap(CompressionUtil.ungzip(compressed));

			Collection<GameObject> areaObjects = parseArea(areas[i], uncompressed);
			objects.addAll(areaObjects);
		}

		return objects.toArray(new GameObject[objects.size()]);
	}

	/**
	 * Parses a single area from the specified buffer.
	 * 
	 * @param area The identifier of that area.
	 * @param buffer The buffer which holds the area's data.
	 * @return A collection of all parsed objects.
	 */
	private Collection<GameObject> parseArea(int area, ByteBuffer buffer) {
		List<GameObject> objects = new ArrayList<>();

		int x = (area >> 8 & 0xFF) * 64;
		int y = (area & 0xFF) * 64;

		int id = -1;
		int idOffset;

		while ((idOffset = BufferUtil.readSmart(buffer)) != 0) {
			id += idOffset;

			int position = 0;
			int positionOffset;

			while ((positionOffset = BufferUtil.readSmart(buffer)) != 0) {
				position += positionOffset - 1;

				int localX = position >> 6 & 0x3F;
				int localY = position & 0x3F;
				int height = position >> 12;

				int info = buffer.get() & 0xFF;
				int type = info >> 2;
				int rotation = info & 3;
				if (type >= 0 && type <= 3 || type >= 9 && type <= 21) {
					Position pos = new Position(x + localX, y + localY, height);

					GameObject object = new GameObject(id, pos, type, rotation);
					objects.add(object);
				}
			}
		}

		return objects;
	}

}