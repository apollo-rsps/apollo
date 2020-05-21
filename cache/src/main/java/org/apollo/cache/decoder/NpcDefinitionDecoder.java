package org.apollo.cache.decoder;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.apollo.cache.Cache;
import org.apollo.cache.CacheBuffer;
import org.apollo.cache.Archive;
import org.apollo.cache.RSFile;
import org.apollo.cache.def.NpcDefinition;

/**
 * Decodes npc data from the {@code npc.dat} file into {@link NpcDefinition}s.
 *
 * @author Major
 */
public final class NpcDefinitionDecoder implements Runnable {

	/**
	 * The IndexedFileSystem.
	 */
	private final Cache cache;

	/**
	 * Creates the NpcDefinitionDecoder.
	 *
	 * @param cache The {@link Archive}.
	 */
	public NpcDefinitionDecoder(Cache cache) {
		this.cache = cache;


	}

	@Override
	public void run() {
		final var fs = cache.getArchive(2);
		final var folder = fs.findFolderByID(9);
		NpcDefinition[] definitions = new NpcDefinition[folder.getHighestId() + 1];
		for (RSFile file : folder.getRSFiles()) {
			definitions[file.getID()] = decode(file.getID(), file.getData());
		}
		NpcDefinition.init(definitions);
	}

	/**
	 * Decodes a single definition.
	 *
	 * @param id     The npc's id.
	 * @param buffer The buffer.
	 * @return The {@link NpcDefinition}.
	 */
	private NpcDefinition decode(int id, CacheBuffer buffer) {
		NpcDefinition definition = new NpcDefinition(id);

		int length;
		int index;
		for (; ; ) {
			int opcode = buffer.readUByte();
			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				length = buffer.readUByte();
				definition.setModels(new int[length]);

				for (index = 0; index < length; ++index) {
					definition.getModels()[index] = buffer.readUShort();
				}

			} else if (opcode == 2) {
				definition.setName(buffer.readString());
			} else if (opcode == 12) {
				definition.setSize(buffer.readUByte());
			} else if (opcode == 13) {
				definition.setStandAnimation(buffer.readUShort());
			} else if (opcode == 14) {
				definition.setWalkAnimation(buffer.readUShort());
			} else if (opcode == 15) {
				definition.setTurnLeftSequence(buffer.readUShort());
			} else if (opcode == 16) {
				definition.setTurnRightSequence(buffer.readUShort());
			} else if (opcode == 17) {
				definition.setWalkAnimations(buffer.readUShort(), buffer.readUShort(), buffer.readUShort(),
						buffer.readUShort());
			} else if (opcode >= 30 && opcode < 35) {
				String action = buffer.readString();
				if (action.equals("hidden")) {
					action = null;
				}

				definition.setInteraction(opcode - 30, action);
			} else if (opcode == 40) {
				length = buffer.readUByte();
				definition.setOriginalColours(new short[length]);
				definition.setReplacementColours(new short[length]);

				for (index = 0; index < length; ++index) {
					definition.getOriginalColours()[index] = (short) buffer.readUShort();
					definition.getReplacementColours()[index] = (short) buffer.readUShort();
				}

			} else if (opcode == 41) {
				length = buffer.readUByte();
				definition.setOriginalTextures(new short[length]);
				definition.setReplacementTextures(new short[length]);

				for (index = 0; index < length; ++index) {
					definition.getOriginalTextures()[index] = (short) buffer.readUShort();
					definition.getReplacementTextures()[index] = (short) buffer.readUShort();
				}

			} else if (opcode == 60) {
				length = buffer.readUByte();
				definition.setHeadModels(new int[length]);

				for (index = 0; index < length; ++index) {
					definition.getHeadModels()[index] = buffer.readUShort();
				}

			} else if (opcode == 93) {
				definition.setDrawMapdot(false);
			} else if (opcode == 95) {
				definition.setCombatLevel(buffer.readUShort());
			} else if (opcode == 97) {
				definition.setScaleX(buffer.readUShort());
			} else if (opcode == 98) {
				definition.setScaleY(buffer.readUShort());
			} else if (opcode == 99) {
				definition.setPriority(true);
			} else if (opcode == 100) {
				definition.setAmbience(buffer.readByte());
			} else if (opcode == 101) {
				definition.setContrast(buffer.readByte());
			} else if (opcode == 102) {
				definition.setHeadIcon(buffer.readUShort());
			} else if (opcode == 103) {
				definition.setRotation(buffer.readUShort());
			} else if (opcode == 106) {
				definition.setVarbit(buffer.readUShort());
				if ('\uffff' == definition.getVarbit()) {
					definition.setVarbit(-1);
				}

				definition.setVarp(buffer.readUShort());
				if ('\uffff' == definition.getVarp()) {
					definition.setVarp(-1);
				}

				length = buffer.readUByte();
				definition.setMorphisms(new int[length + 2]);

				for (index = 0; index <= length; ++index) {
					definition.getMorphisms()[index] = buffer.readUShort();
					if (definition.getMorphisms()[index] == 0xFFFF) {
						definition.getMorphisms()[index] = -1;
					}
				}

				definition.getMorphisms()[length + 1] = -1;

			} else if (opcode == 107) {
				definition.setClickable(false);
			} else if (opcode == 109) {
				definition.setSlowWalk(false);
			} else if (opcode == 111) {
				definition.setAnimateIdle(true);
			} else if (opcode == 118) {
				definition.setVarbit(buffer.readUShort());
				if ('\uffff' == definition.getVarbit()) {
					definition.setVarbit(-1);
				}

				definition.setVarp(buffer.readUShort());
				if ('\uffff' == definition.getVarp()) {
					definition.setVarp(-1);
				}

				int var = buffer.readUShort();
				if (var == 0xFFFF) {
					var = -1;
				}

				length = buffer.readUByte();
				definition.setMorphisms(new int[length + 2]);

				for (index = 0; index <= length; ++index) {
					definition.getMorphisms()[index] = buffer.readUShort();
					if (definition.getMorphisms()[index] == '\uffff') {
						definition.getMorphisms()[index] = -1;
					}
				}

				definition.getMorphisms()[length + 1] = var;
			} else if (opcode == 249) {
				length = buffer.readUByte();

				definition.setParameters(new Int2ObjectArrayMap<>(length));

				for (int i = 0; i < length; i++) {
					boolean isString = buffer.readUByte() == 1;
					int key = buffer.readUMedInt();
					Object value;

					if (isString) {
						value = buffer.readString();
					} else {
						value = buffer.readInt();
					}

					definition.getParameters().put(key, value);
				}
			}
		}
	}

	/**
	 * Wraps a morphism value around, returning -1 if the specified value is 65,535.
	 *
	 * @param value The value.
	 * @return -1 if {@code value} is 65,535, otherwise {@code value}.
	 */
	private int wrap(int value) {
		return value == 65_535 ? -1 : value;
	}

}