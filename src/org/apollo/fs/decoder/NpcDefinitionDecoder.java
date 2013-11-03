package org.apollo.fs.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.util.ByteBufferUtil;

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

		int count = idx.getShort();
		int[] indices = new int[count];
		int index = 2;
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
	private NpcDefinition decode(int id, ByteBuffer buffer) {
		NpcDefinition definition = new NpcDefinition(id);

		while (true) {
			int code = buffer.get() & 0xFF;
			if (code == 0) {
				return definition;
			} else if (code == 1) {
				int length = buffer.get() & 0xFF;
				int[] npcModels = new int[length];
				for (int i = 0; i < length; i++) {
					npcModels[i] = buffer.getShort();
				}
			} else if (code == 2) {
				definition.setName(ByteBufferUtil.readString(buffer));
			} else if (code == 3) {
				definition.setDescription(ByteBufferUtil.readString(buffer));
			} else if (code == 12) {
				definition.setSize(buffer.get());
			} else if (code == 13) {
				definition.setStandAnimation(buffer.getShort());
			} else if (code == 14) {
				definition.setWalkAnimation(buffer.getShort());
			} else if (code == 17) {
				definition
						.setWalkAnimations(buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer.getShort());
			} else if (code >= 30 && code < 40) {
				String str = ByteBufferUtil.readString(buffer);
				if (str.equals("hidden")) {
					str = null;
				}
				definition.setInteraction(code - 30, str);
			} else if (code == 40) {
				int length = buffer.get() & 0xFF;
				int[] originalColours = new int[length];
				int[] replacementColours = new int[length];
				for (int i = 0; i < length; i++) {
					originalColours[i] = buffer.getShort();
					replacementColours[i] = buffer.getShort();
				}
			} else if (code == 60) {
				int length = buffer.get() & 0xFF;
				int[] additionalModels = new int[length];
				for (int i = 0; i < length; i++) {
					additionalModels[i] = buffer.getShort();
				}
			} else if (code == 90) {
				buffer.getShort(); // Dummy
			} else if (code == 91) {
				buffer.getShort(); // Dummy
			} else if (code == 92) {
				buffer.getShort(); // Dummy
			} else if (code == 93) {
				@SuppressWarnings("unused")
				boolean drawMinimapDot = false;
			} else if (code == 95) {
				definition.setCombatLevel(buffer.getShort());
			} else if (code == 97) {
				@SuppressWarnings("unused")
				int scaleXZ = buffer.getShort();
			} else if (code == 98) {
				@SuppressWarnings("unused")
				int scaleY = buffer.getShort();
			} else if (code == 99) {
				@SuppressWarnings("unused")
				boolean unknown = true;
			} else if (code == 100) {
				@SuppressWarnings("unused")
				int lightModifier = buffer.get();
			} else if (code == 101) {
				@SuppressWarnings("unused")
				int shadowModifier = buffer.get() * 5;
			} else if (code == 102) {
				@SuppressWarnings("unused")
				int headIcon = buffer.getShort();
			} else if (code == 103) {
				@SuppressWarnings("unused")
				int degreesToRotate = buffer.getShort();
			} else if (code == 106) {
				int morphVariableBitsIndex = buffer.getShort();
				if (morphVariableBitsIndex == 65535) {
					morphVariableBitsIndex = -1;
				}
				int morphismCount = buffer.getShort();
				if (morphismCount == 65535) {
					morphismCount = -1;
				}

				int count = buffer.get() & 0xFF;
				int[] morphisms = new int[count + 1];
				for (int i = 0; i <= count; i++) {
					int morphism = buffer.getShort();
					if (morphism == 65535) {
						morphism = -1;
					}
					morphisms[i] = morphism;
				}
			} else if (code == 107) {
				@SuppressWarnings("unused")
				boolean clickable = false;
			}
		}
	}

}