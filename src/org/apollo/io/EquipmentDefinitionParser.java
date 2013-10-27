package org.apollo.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apollo.game.model.def.EquipmentDefinition;

/**
 * A class which parses the {@code data/equipment-[release].dat} file to
 * create an array of {@link EquipmentDefinition}s.
 * @author Graham
 */
public final class EquipmentDefinitionParser {

	/**
	 * The input stream.
	 */
	private final InputStream is;

	/**
	 * Creates the equipment definition parser.
	 * @param is The input stream.
	 */
	public EquipmentDefinitionParser(InputStream is) {
		this.is = is;
	}

	/**
	 * Parses the input stream.
	 * @return The equipment definition array.
	 * @throws IOException if an I/O error occurs.
	 */
	public EquipmentDefinition[] parse() throws IOException {
		DataInputStream dis = new DataInputStream(is);

		int count = dis.readShort() & 0xFFFF;
		EquipmentDefinition[] defs = new EquipmentDefinition[count];

		for (int id = 0; id < count; id++) {
			int slot = dis.readByte() & 0xFF;
			if (slot != 0xFF) {
				boolean twoHanded = dis.readBoolean();
				boolean fullBody = dis.readBoolean();
				boolean fullHat = dis.readBoolean();
				boolean fullMask = dis.readBoolean();
				int attack = dis.readByte() & 0xFF;
				int strength = dis.readByte() & 0xFF;
				int defence = dis.readByte() & 0xFF;
				int ranged = dis.readByte() & 0xFF;
				int magic = dis.readByte() & 0xFF;

				EquipmentDefinition def = new EquipmentDefinition(id);
				def.setLevels(attack, strength, defence, ranged, magic);
				def.setSlot(slot);
				def.setFlags(twoHanded, fullBody, fullHat, fullMask);

				defs[id] = def;
			}
		}

		return defs;
	}

}
