package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.util.ByteBufferUtil;

/**
 * A class which parses item definitions.
 * @author Graham
 */
public final class ItemDefinitionParser {

	/**
	 * The indexed file system.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the item definition parser.
	 * @param fs The indexed file system.
	 */
	public ItemDefinitionParser(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Parses the item definitions.
	 * @return The item definitions.
	 * @throws IOException if an I/O error occurs.
	 */
	public ItemDefinition[] parse() throws IOException {
		Archive config = Archive.decode(fs.getFile(0, 2));
		ByteBuffer dat = config.getEntry("obj.dat").getBuffer();
		ByteBuffer idx = config.getEntry("obj.idx").getBuffer();

		int count = idx.getShort();
		int[] indices = new int[count];
		int index = 2;
		for (int i = 0; i < count; i++) {
			indices[i] = index;
			index += idx.getShort();
		}

		ItemDefinition[] defs = new ItemDefinition[count];
		for (int i = 0; i < count; i++) {
			dat.position(indices[i]);
			defs[i] = parseDefinition(i, dat);
		}

		return defs;
	}

	/**
	 * Parses a single definition.
	 * @param id The item's id.
	 * @param buffer The buffer.
	 * @return The definition.
	 */
	private ItemDefinition parseDefinition(int id, ByteBuffer buffer) {
		ItemDefinition def = new ItemDefinition(id);

		while (true) {
			int code = buffer.get() & 0xFF;

			if (code == 0) {
				return def;
			} else if (code == 1) {
				@SuppressWarnings("unused")
				int modelId = buffer.getShort() & 0xFFFF;
			} else if (code == 2) {
				def.setName(ByteBufferUtil.readString(buffer));
			} else if (code == 3) {
				def.setDescription(ByteBufferUtil.readString(buffer));
			} else if (code == 4) {
				@SuppressWarnings("unused")
				int modelScale = buffer.getShort() & 0xFFFF;
			} else if (code == 5) {
				@SuppressWarnings("unused")
				int modelRotationX = buffer.getShort() & 0xFFFF;
			} else if (code == 6) {
				@SuppressWarnings("unused")
				int modelRotationY = buffer.getShort() & 0xFFFF;
			} else if (code == 7) {
				@SuppressWarnings("unused")
				int modelTransformationX = buffer.getShort();
			} else if (code == 8) {
				@SuppressWarnings("unused")
				int modelTransformationY = buffer.getShort();
			} else if (code == 10) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 11) {
				def.setStackable(true);
			} else if (code == 12) {
				def.setValue(buffer.getInt());
			} else if (code == 16) {
				def.setMembersOnly(true);
			} else if (code == 23) {
				@SuppressWarnings("unused")
				int unknownShort = buffer.getShort() & 0xFFFF;
				@SuppressWarnings("unused")
				int unknownByte = buffer.get();
			} else if (code == 24) {
				@SuppressWarnings("unused")
				int unknownShort = buffer.getShort() & 0xFFFF;
			} else if (code == 25) {
				@SuppressWarnings("unused")
				int unknownShort = buffer.getShort() & 0xFFFF;
				@SuppressWarnings("unused")
				int unknownByte = buffer.get();
			} else if (code == 26) {
				@SuppressWarnings("unused")
				int unknownShort = buffer.getShort() & 0xFFFF;
			} else if (code >= 30 && code < 35) {
				String str = ByteBufferUtil.readString(buffer);
				if (str.equalsIgnoreCase("hidden")) {
					str = null;
				}
				def.setGroundAction(code - 30, str);
			} else if (code >= 35 && code < 40) {
				String str = ByteBufferUtil.readString(buffer);
				def.setInventoryAction(code - 35, str);
			} else if (code == 40) {
				int colorCount = buffer.get() & 0xFF;
				for (int i = 0; i < colorCount; i++) {
					@SuppressWarnings("unused")
					int oldColor = buffer.getShort() & 0xFFFF;
					@SuppressWarnings("unused")
					int newColor = buffer.getShort() & 0xFFFF;
				}
			} else if (code == 78) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 79) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 90) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 91) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 92) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 93) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 95) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 97) {
				int noteInfoId = buffer.getShort() & 0xFFFF;
				def.setNoteInfoId(noteInfoId);
			} else if (code == 98) {
				int noteGraphicId = buffer.getShort() & 0xFFFF;
				def.setNoteGraphicId(noteGraphicId);
			} else if (code >= 100 && code < 110) {
				@SuppressWarnings("unused")
				int stackId = buffer.getShort() & 0xFFFF;
				@SuppressWarnings("unused")
				int stackAmount = buffer.getShort() & 0xFFFF;
			} else if (code == 110) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 111) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 112) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() & 0xFFFF;
			} else if (code == 114) {
				@SuppressWarnings("unused")
				int unknown = buffer.getShort() * 5;
			} else if (code == 115) {
				@SuppressWarnings("unused")
				int team = buffer.get() & 0xFF;
			}
		}
	}

}
