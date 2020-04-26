package org.apollo.cache.decoder;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.apollo.cache.Cache;
import org.apollo.cache.CacheBuffer;
import org.apollo.cache.Archive;
import org.apollo.cache.RSFile;
import org.apollo.cache.def.ObjectDefinition;

import java.nio.ByteBuffer;

/**
 * Decodes object data from the {@code loc.dat} file into {@link ObjectDefinition}s.
 *
 * @author Major
 */
public final class ObjectDefinitionDecoder implements Runnable {

	/**
	 * The IndexedFileSystem.
	 */
	private final Cache cache;

	/**
	 * Creates the ObjectDefinitionDecoder.
	 *
	 * @param cache The {@link Archive}.
	 */
	public ObjectDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		final var fs = cache.getArchive(2);
		final var folder = fs.findFolderByID(6);
		ObjectDefinition[] definitions = new ObjectDefinition[folder.getHighestId() + 1];
		for (RSFile file : folder.getRSFiles()) {
			definitions[file.getID()] = decode(file.getID(), file.getData());
		}
		ObjectDefinition.init(definitions);
	}

	/**
	 * Decodes data from the cache into an {@link ObjectDefinition}.
	 *
	 * @param id     The id of the object.
	 * @param buffer The {@link ByteBuffer} containing the data.
	 * @return The object definition.
	 */
	private ObjectDefinition decode(int id, CacheBuffer buffer) {
		ObjectDefinition definition = new ObjectDefinition(id);
		for (; ; ) {
			final var opcode = buffer.readUByte();
			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				int count = buffer.readUByte();
				if (count > 0) {
					definition.setTypes(new int[count]);
					definition.setModels(new int[count][1]);
					for (int index = 0; index < count; index++) {
						definition.getModels()[index][0] = buffer.readUShort();
						definition.getTypes()[index] = buffer.readUByte();
					}
				}
			} else if (opcode == 2) {
				definition.setName(buffer.readString());
			} else if (opcode == 5) {
				int count = buffer.readUByte();
				if (count > 0) {
					definition.setTypes(null);
					definition.setModels(new int[count][1]);
					for (int index = 0; index < count; index++)
						definition.getModels()[index][0] = buffer.readUShort();
				}
			} else if (opcode == 14) {
				definition.setWidth(buffer.readUByte());
			} else if (opcode == 15) {
				definition.setLength(buffer.readUByte());
			} else if (opcode == 17) {
				definition.setSolid(false);
			} else if (opcode == 18) {
				definition.setImpenetrable(false);
			} else if (opcode == 19) {
				definition.setInteractive(buffer.readUByte() == 1);
			} else if (opcode == 21) {
				definition.setAdjustValue(0);
			} else if (opcode == 22) {
				definition.setDynamicShading(true);
			} else if (opcode == 23) {
				definition.setOccludes((byte) 1);
			} else if (opcode == 24) {
				definition.setAnimations(new int[]{buffer.readUShort()});
				if (definition.getAnimations()[0] == 65535) {
					definition.getAnimations()[0] = -1;
				}
			} else if (opcode == 27) {
				definition.setCollisionType(1);
			} else if (opcode == 28) {
				definition.setDecorationDisplacement(buffer.readUByte());
			} else if (opcode == 29) {
				definition.setContrast(buffer.readSignedByte());
			} else if (opcode == 39) {
				definition.setAmbience(buffer.readSignedByte());
			} else if (opcode >= 30 && opcode < 35) {
				String[] actions = definition.getMenuActions();
				if (actions == null) {
					actions = new String[10];
				}
				actions[opcode - 30] = buffer.readString();
				definition.setMenuActions(actions);
			} else if (opcode == 40) {
				final int count = buffer.readUByte();
				definition.setOriginalColours(new short[count]);
				definition.setReplacementColours(new short[count]);
				for (int i = 0; i < count; i++) {
					definition.getOriginalColours()[i] = (short) buffer.readUShort();
					definition.getReplacementColours()[i] = (short) buffer.readUShort();
				}
			} else if (opcode == 41) {
				final int count = buffer.readUByte();
				definition.setOriginalTextures(new short[count]);
				definition.setReplacementTextures(new short[count]);
				for (int i_7_ = 0; i_7_ < count; i_7_++) {
					definition.getOriginalTextures()[i_7_] = (short) buffer.readUShort();
					definition.getReplacementTextures()[i_7_] = (short) buffer.readUShort();
				}
			} else if (opcode == 62) {
				definition.setMirrorModel(true);
			} else if (opcode == 64) {
				definition.setCastsShadow(false);
			} else if (opcode == 65) {
				definition.setScaleX(buffer.readUShort());
			} else if (opcode == 66) {
				definition.setScaleY(buffer.readUShort());
			} else if (opcode == 67) {
				definition.setScaleZ(buffer.readUShort());
			} else if (opcode == 68) {
				definition.setMapscene(buffer.readUShort());
			} else if (opcode == 69) {
				definition.setRotationFlag(buffer.readUByte());
			} else if (opcode == 70) {
				definition.setTranslateX((buffer.readSignedShort()));
			} else if (opcode == 71) {
				definition.setTranslateY((buffer.readSignedShort()));
			} else if (opcode == 72) {
				definition.setTranslateZ((buffer.readSignedShort()));
			} else if (opcode == 73) {
				definition.setDecoration(true);
			} else if (opcode == 74) {
				definition.setObstructive(true);
			} else if (opcode == 75) {
				definition.setHoldsItemPiles(buffer.readUByte());
			} else if (opcode == 77 || opcode == 92) {
				definition.setVarbit(buffer.readUShort());
				if (65535 == definition.getVarbit()) {
					definition.setVarbit(-1);
				}
				definition.setVarp(buffer.readUShort());
				if (65535 == definition.getVarp()) {
					definition.setVarp(-1);
				}

				int model = -1;
				if (opcode == 92) {
					model = buffer.readSmart32();
				}

				int length = buffer.readUByte();
				definition.setMorphisms(new int[length + 2]);

				for (int index = 0; index <= length; index++) {
					definition.getMorphisms()[index] = buffer.readSmart32();
				}

				definition.getMorphisms()[1 + length] = model;
			} else if (opcode == 78) {
				definition.setSound(buffer.readUShort());
				definition.setSoundRadius(buffer.readUByte());
			} else if (opcode == 79) {
				definition.setSoundDelayMinDuration(buffer.readUShort());
				definition.setSoundDelayMaxDuration(buffer.readUShort());
				definition.setSoundRadius(buffer.readUByte());
				final int count = buffer.readUByte();
				definition.setSounds(new int[count]);
				for (int i = 0; i < count; i++)
					definition.getSounds()[i] = buffer.readUShort();
			} else if (opcode == 81) {
				definition.setAdjustValue(buffer.readUByte() * 256);
			} else if (opcode == 82) {
				definition.setMapSceneType(buffer.readUShort());
			} else if (opcode == 249) {
				final int size = buffer.readUByte();
				definition.setParameters(new Int2ObjectArrayMap<>(size));

				for (int index = 0; size > index; index++) {
					final boolean stringInstance = buffer.readUByte() == 1;
					final int key = buffer.readUMedInt();
					final Object value;
					if (stringInstance) {
						value = buffer.readString();
					} else {
						value = buffer.readInt();
					}
					definition.getParameters().put(key, value);
				}
			}
		}
	}
}