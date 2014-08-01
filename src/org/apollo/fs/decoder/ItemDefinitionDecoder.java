package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.util.BufferUtil;

/**
 * Decodes item data from the {@code obj.dat} file into {@link ItemDefinition}s.
 * 
 * @author Graham
 */
public final class ItemDefinitionDecoder {

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the item definition decoder.
	 * 
	 * @param fs The indexed file system.
	 */
	public ItemDefinitionDecoder(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Decodes the item definitions.
	 * 
	 * @return The item definitions.
	 * @throws IOException If an I/O error occurs.
	 */
	public ItemDefinition[] decode() throws IOException {
		Archive config = Archive.decode(fs.getFile(0, 2));
		ByteBuffer data = config.getEntry("obj.dat").getBuffer();
		ByteBuffer idx = config.getEntry("obj.idx").getBuffer();

		int count = idx.getShort(), index = 2;
		int[] indices = new int[count];
		for (int i = 0; i < count; i++) {
			indices[i] = index;
			index += idx.getShort();
		}

		ItemDefinition[] defs = new ItemDefinition[count];
		for (int i = 0; i < count; i++) {
			data.position(indices[i]);
			defs[i] = decode(i, data);
		}

		return defs;
	}

	/**
	 * Decodes a single definition.
	 * 
	 * @param id The item's id.
	 * @param buffer The buffer.
	 * @return The {@link ItemDefinition}.
	 */
	private ItemDefinition decode(int id, ByteBuffer buffer) {
		ItemDefinition definition = new ItemDefinition(id);
		while (true) {
			int opcode = buffer.get() & 0xFF;

			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				buffer.getShort();
			} else if (opcode == 2) {
				definition.setName(BufferUtil.readString(buffer));
			} else if (opcode == 3) {
				definition.setDescription(BufferUtil.readString(buffer));
			} else if (opcode == 4) {
				buffer.getShort();
			} else if (opcode == 5) {
				buffer.getShort();
			} else if (opcode == 6) {
				buffer.getShort();
			} else if (opcode == 7) {
				buffer.getShort();
			} else if (opcode == 8) {
				buffer.getShort();
			} else if (opcode == 10) {
				buffer.getShort();
			} else if (opcode == 11) {
				definition.setStackable(true);
			} else if (opcode == 12) {
				definition.setValue(buffer.getInt());
			} else if (opcode == 16) {
				definition.setMembersOnly(true);
			} else if (opcode == 23) {
				buffer.getShort();
				buffer.get();
			} else if (opcode == 24) {
				buffer.getShort();
			} else if (opcode == 25) {
				buffer.getShort();
				buffer.get();
			} else if (opcode == 26) {
				buffer.getShort();
			} else if (opcode >= 30 && opcode < 35) {
				String str = BufferUtil.readString(buffer);
				if (str.equalsIgnoreCase("hidden")) {
					str = null;
				}
				definition.setGroundAction(opcode - 30, str);
			} else if (opcode >= 35 && opcode < 40) {
				definition.setInventoryAction(opcode - 35, BufferUtil.readString(buffer));
			} else if (opcode == 40) {
				int colourCount = buffer.get() & 0xFF;
				for (int i = 0; i < colourCount; i++) {
					buffer.getShort();
					buffer.getShort();
				}
			} else if (opcode == 78) {
				buffer.getShort();
			} else if (opcode == 79) {
				buffer.getShort();
			} else if (opcode == 90) {
				buffer.getShort();
			} else if (opcode == 91) {
				buffer.getShort();
			} else if (opcode == 92) {
				buffer.getShort();
			} else if (opcode == 93) {
				buffer.getShort();
			} else if (opcode == 95) {
				buffer.getShort();
			} else if (opcode == 97) {
				definition.setNoteInfoId(buffer.getShort() & 0xFFFF);
			} else if (opcode == 98) {
				definition.setNoteGraphicId(buffer.getShort() & 0xFFFF);
			} else if (opcode >= 100 && opcode < 110) {
				buffer.getShort();
				buffer.getShort();
			} else if (opcode == 110) {
				buffer.getShort();
			} else if (opcode == 111) {
				buffer.getShort();
			} else if (opcode == 112) {
				buffer.getShort();
			} else if (opcode == 113) {
				buffer.get();
			} else if (opcode == 114) {
				buffer.get();
			} else if (opcode == 115) {
				definition.setTeam(buffer.get() & 0xFF);
			}
		}
	}

}