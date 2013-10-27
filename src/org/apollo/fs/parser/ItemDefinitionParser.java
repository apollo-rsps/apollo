package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.util.ByteBufferUtil;

/**
 * A class which parses item definitions.
 * 
 * @author Graham
 */
public final class ItemDefinitionParser {

	/**
	 * The indexed file system.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the item definition parser.
	 * 
	 * @param fs
	 *            The indexed file system.
	 */
	public ItemDefinitionParser(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Parses the item definitions.
	 * 
	 * @return The item definitions.
	 * @throws IOException
	 *             if an I/O error occurs.
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
	 * 
	 * @param id
	 *            The item's id.
	 * @param buffer
	 *            The buffer.
	 * @return The definition.
	 */
	private ItemDefinition parseDefinition(int id, ByteBuffer buffer) {
		ItemDefinition def = new ItemDefinition(id);
		while (true) {
			int code = buffer.get() & 0xFF;

			if (code == 0) {
				return def;
			} else if (code == 1) {
				buffer.getShort();// & 0xFFFF; // model Id
			} else if (code == 2) {
				def.setName(ByteBufferUtil.readString(buffer));
			} else if (code == 3) {
				def.setDescription(ByteBufferUtil.readString(buffer));
			} else if (code == 4) {
				buffer.getShort();// & 0xFFFF; // sprite scale
			} else if (code == 5) {
				buffer.getShort();// & 0xFFFF; // sprite pitch
			} else if (code == 6) {
				buffer.getShort();// & 0xFFFF; //sprite camera roll
			} else if (code == 7) {
				buffer.getShort(); // sprite dX
			} else if (code == 8) {
				buffer.getShort(); // sprite dY
			} else if (code == 10) {
				buffer.getShort();// & 0xFFFF;
			} else if (code == 11) {
				def.setStackable(true);
			} else if (code == 12) {
				buffer.getInt(); // model height
			} else if (code == 16) {
				def.setMembersOnly(true);
			} else if (code == 23) {
				buffer.getShort(); // & 0xFFFF; //primaryMaleEquipModelId
				buffer.get(); // maleEquipYTranslation
			} else if (code == 24) {
				buffer.getShort(); // & 0xFFFF; // secondaryMaleEquipModelId
			} else if (code == 25) {
				buffer.getShort(); // & 0xFFFF; // primaryFemaleEquipModelId
				buffer.get(); // femaleEquipYTranslation
			} else if (code == 26) {
				buffer.getShort(); // & 0xFFFF; // secondaryFemaleEquipModelId
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
				int colourCount = buffer.get() & 0xFF;
				for (int i = 0; i < colourCount; i++) {
					buffer.getShort(); // & 0xFFFF; // original colour
					buffer.getShort(); // & 0xFFFF; // replacement colour
				}
			} else if (code == 78) {
				buffer.getShort(); // & 0xFFFF; // tertiaryMaleEquipModelId
			} else if (code == 79) {
				buffer.getShort(); // & 0xFFFF; // tertiaryFemaleEquipModelId
			} else if (code == 90) {
				buffer.getShort(); // & 0xFFFF; // primaryMaleHeadPiece
			} else if (code == 91) {
				buffer.getShort(); // & 0xFFFF; // primaryFemaleHeadPiece
			} else if (code == 92) {
				buffer.getShort(); // & 0xFFFF; // secondaryMaleHeadPiece
			} else if (code == 93) {
				buffer.getShort(); // & 0xFFFF; // secondaryFemaleHeadPiece
			} else if (code == 95) {
				buffer.getShort(); // & 0xFFFF; // spriteCameraYaw
			} else if (code == 97) {
				int noteInfoId = buffer.getShort() & 0xFFFF;
				def.setNoteInfoId(noteInfoId);
			} else if (code == 98) {
				int noteGraphicId = buffer.getShort() & 0xFFFF;
				def.setNoteGraphicId(noteGraphicId);
			} else if (code >= 100 && code < 110) {
				buffer.getShort(); // & 0xFFFF; // stack id
				buffer.getShort(); // & 0xFFFF; // stack size
			} else if (code == 110) {
				buffer.getShort(); // & 0xFFFF; // groundScaleX
			} else if (code == 111) {
				buffer.getShort(); // & 0xFFFF; // groundScaleY
			} else if (code == 112) {
				buffer.getShort(); // & 0xFFFF; // groundScaleZ
			} else if (code == 113) {
				buffer.get(); // light ambiance
			} else if (code == 114) {
				buffer.getShort(); // * 5; // light diffusion
			} else if (code == 115) {
				buffer.get(); // & 0xFF; // team
			}
		}
	}

}
