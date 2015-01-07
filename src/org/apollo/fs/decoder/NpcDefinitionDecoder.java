package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.util.BufferUtil;

/**
 * Decodes npc data from the {@code npc.dat} file into {@link NpcDefinition}s.
 * 
 * @author Major
 */
public final class NpcDefinitionDecoder {

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the npc definition decoder.
	 * 
	 * @param fs The indexed file system.
	 */
	public NpcDefinitionDecoder(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Decodes the npc definitions.
	 * 
	 * @return An array of all parsed npc definitions.
	 * @throws IOException If an I/O error occurs.
	 */
	public NpcDefinition[] decode() throws IOException {
		Archive config = Archive.decode(fs.getFile(0, 2));
		ByteBuffer data = config.getEntry("npc.dat").getBuffer();
		ByteBuffer idx = config.getEntry("npc.idx").getBuffer();

		int count = idx.getShort(), index = 2;
		int[] indices = new int[count];
		for (int i = 0; i < count; i++) {
			indices[i] = index;
			index += idx.getShort();
		}

		NpcDefinition[] defs = new NpcDefinition[count];
		for (int i = 0; i < count; i++) {
			data.position(indices[i]);
			defs[i] = decode(i, data);
		}

		return defs;
	}

	/**
	 * Decodes a single definition.
	 * 
	 * @param id The npc's id.
	 * @param buffer The buffer.
	 * @return The {@link NpcDefinition}.
	 */
	private static NpcDefinition decode(int id, ByteBuffer buffer) {
		NpcDefinition definition = new NpcDefinition(id);

		while (true) {
			int opcode = buffer.get() & 0xFF;

			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				int length = buffer.get() & 0xFF;
				int[] models = new int[length];
				for (int i = 0; i < length; i++) {
					models[i] = buffer.getShort();
				}
			} else if (opcode == 2) {
				definition.setName(BufferUtil.readString(buffer));
			} else if (opcode == 3) {
				definition.setDescription(BufferUtil.readString(buffer));
			} else if (opcode == 12) {
				definition.setSize(buffer.get());
			} else if (opcode == 13) {
				definition.setStandAnimation(buffer.getShort());
			} else if (opcode == 14) {
				definition.setWalkAnimation(buffer.getShort());
			} else if (opcode == 17) {
				definition.setWalkAnimations(buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer.getShort());
			} else if (opcode >= 30 && opcode < 40) {
				String str = BufferUtil.readString(buffer);
				if (str.equals("hidden")) {
					str = null;
				}
				definition.setInteraction(opcode - 30, str);
			} else if (opcode == 40) {
				int length = buffer.get() & 0xFF;
				int[] originalColours = new int[length];
				int[] replacementColours = new int[length];
				for (int i = 0; i < length; i++) {
					originalColours[i] = buffer.getShort();
					replacementColours[i] = buffer.getShort();
				}
			} else if (opcode == 60) {
				int length = buffer.get() & 0xFF;
				int[] additionalModels = new int[length];
				for (int i = 0; i < length; i++) {
					additionalModels[i] = buffer.getShort();
				}
			} else if (opcode >= 90 && opcode <= 92) {
				buffer.getShort(); // Dummy
			} else if (opcode == 95) {
				definition.setCombatLevel(buffer.getShort());
			} else if (opcode == 97 || opcode == 98) {
				buffer.getShort();
			} else if (opcode == 100 || opcode == 101) {
				buffer.get();
			} else if (opcode == 102 || opcode == 103) {
				buffer.getShort();
			} else if (opcode == 106) {
				@SuppressWarnings("unused")
				int morphVariableBitsIndex = wrapMorphism(buffer.getShort());
				@SuppressWarnings("unused")
				int morphismCount = wrapMorphism(buffer.getShort());

				int count = buffer.get() & 0xFF;
				int[] morphisms = new int[count + 1];
				Arrays.setAll(morphisms, index -> wrapMorphism(buffer.getShort()));
			} else if (opcode == 107) {
				@SuppressWarnings("unused")
				boolean clickable = false;
			}
		}
	}

	/**
	 * Wraps a morphism value around, returning -1 if the specified value is 65,535. TODO name
	 * 
	 * @param value The value.
	 * @return -1 if {@code value} is 65,535, otherwise {@code value}.
	 */
	private static int wrapMorphism(int value) {
		return value == 65_535 ? -1 : value;
	}

}