package org.apollo.game.io;

import org.apollo.cache.def.EquipmentDefinition;

import java.io.*;

/**
 * A class that parses the {@code data/equipment-[release].dat} file to create an array of {@link
 * EquipmentDefinition}s.
 *
 * @author Graham
 */
public final class EquipmentDefinitionParser implements Runnable {

	/**
	 * Creates an {@link EquipmentDefinitionParser} that reads from the file located at the specified {@code path}.
	 *
	 * @param path The path to the file.
	 * @return The EquipmentDefinitionParser.
	 * @throws UncheckedIOException If there is an error creating the {@link FileInputStream}.
	 */
	public static EquipmentDefinitionParser fromFile(String path) {
		try {
			return new EquipmentDefinitionParser(new BufferedInputStream(new FileInputStream(path)));
		} catch (IOException e) {
			throw new UncheckedIOException("Error creating EquipmentDefinitionParser.", e);
		}
	}

	/**
	 * The InputStream.
	 */
	private final InputStream is;

	/**
	 * Creates the EquipmentDefinitionParser.
	 *
	 * @param is The {@link InputStream}.
	 */
	public EquipmentDefinitionParser(InputStream is) {
		this.is = is;
	}

	@Override
	public void run() {
		try (DataInputStream in = new DataInputStream(is)) {
			int count = in.readShort() & 0xFFFF;
			EquipmentDefinition[] definitions = new EquipmentDefinition[count];

			for (int id = 0; id < count; id++) {
				int slot = in.readByte() & 0xFF;
				if (slot != 0xFF) {
					boolean twoHanded = in.readBoolean();
					boolean fullBody = in.readBoolean();
					boolean fullHat = in.readBoolean();
					boolean fullMask = in.readBoolean();
					int attack = in.readByte() & 0xFF;
					int strength = in.readByte() & 0xFF;
					int defence = in.readByte() & 0xFF;
					int ranged = in.readByte() & 0xFF;
					int prayer = in.readByte() & 0xFF;
					int magic = in.readByte() & 0xFF;

					EquipmentDefinition definition = new EquipmentDefinition(id);
					definition.setLevels(attack, strength, defence, ranged, prayer, magic);
					definition.setSlot(slot);
					definition.setFlags(twoHanded, fullBody, fullHat, fullMask);

					definitions[id] = definition;
				}
			}

			EquipmentDefinition.init(definitions);
		} catch (IOException e) {
			throw new UncheckedIOException("Error parsing EquipmentDefinitions.", e);
		}
	}

}