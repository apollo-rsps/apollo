package org.apollo.cache.decoder;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.apollo.cache.Cache;
import org.apollo.cache.CacheBuffer;
import org.apollo.cache.Archive;
import org.apollo.cache.RSFile;
import org.apollo.cache.def.ItemDefinition;

/**
 * Decodes item data from the {@code obj.dat} file into {@link ItemDefinition}s.
 *
 * @author Graham
 */
public final class ItemDefinitionDecoder implements Runnable {

	/**
	 * The  IndexedFileSystem.
	 */
	private final Cache cache;

	/**
	 * Creates the ItemDefinitionDecoder.
	 *
	 * @param cache The {@link Archive}.
	 */
	public ItemDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		final var fs = cache.getArchive(2);
		final var folder = fs.findFolderByID(10);

		ItemDefinition[] definitions = new ItemDefinition[folder.getHighestId() + 1];
		for (RSFile file : folder.getRSFiles()) {
			definitions[file.getID()] = decode(file.getID(), file.getData());
		}

		ItemDefinition.init(definitions);
	}

	/**
	 * Decodes a single definition.
	 *
	 * @param id     The item's id.
	 * @param buffer The buffer.
	 * @return The {@link ItemDefinition}.
	 */
	private ItemDefinition decode(int id, CacheBuffer buffer) {
		ItemDefinition definition = new ItemDefinition(id);
		for (; ; ) {
			int opcode = buffer.readUByte();
			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				definition.setBaseModel(buffer.readUShort());
			} else if (opcode == 2) {
				definition.setName(buffer.readString());
			} else if (opcode == 4) {
				definition.setModelScale(buffer.readUShort());
			} else if (opcode == 5) {
				definition.setModelPitch(buffer.readUShort());
			} else if (opcode == 6) {
				definition.setModelRoll(buffer.readUShort());
			} else if (opcode == 7) {
				definition.setModelTranslateX(buffer.readSignedShort());
			} else if (opcode == 8) {
				definition.setModelTranslateY(buffer.readSignedShort());
			} else if (opcode == 11) {
				definition.setStackable(true);
			} else if (opcode == 12) {
				definition.setValue(buffer.readInt());
			} else if (opcode == 16) {
				definition.setMembersOnly(true);
			} else if (opcode == 23) {
				definition.setPrimaryMaleModel(buffer.readUShort());
				definition.setMaleYOffset(buffer.readUByte());
			} else if (opcode == 24) {
				definition.setSecondaryMaleModel(buffer.readUShort());
			} else if (opcode == 25) {
				definition.setPrimaryFemaleModel(buffer.readUShort());
				definition.setFemaleYOffset(buffer.readUByte());
			} else if (opcode == 26) {
				definition.setSecondaryFemaleModel(buffer.readUShort());
			} else if (opcode >= 30 && opcode < 35) {
				String str = buffer.readString();
				if (str.equalsIgnoreCase("hidden")) {
					str = null;
				}
				definition.setGroundAction(opcode - 30, str);
			} else if (opcode >= 35 && opcode < 40) {
				definition.setInventoryAction(opcode - 35, buffer.readString());
			} else if (opcode == 40) {
				int size = buffer.readUByte();
				definition.setOriginalModelColors(new short[size]);
				definition.setModifiedModelColors(new short[size]);
				for (int count = 0; count < size; count++) {
					definition.getOriginalModelColors()[count] = (short) buffer.readUShort();
					definition.getModifiedModelColors()[count] = (short) buffer.readUShort();
				}
			} else if (opcode == 41) {
				int size = buffer.readUByte();
				definition.setOriginalTextureColors(new short[size]);
				definition.setModifiedTextureColors(new short[size]);
				for (int count = 0; count < size; count++) {
					definition.getOriginalTextureColors()[count] = (short) buffer.readUShort();
					definition.getModifiedTextureColors()[count] = (short) buffer.readUShort();
				}
			} else if (opcode == 42) {
				definition.setShiftClickDropIndex(buffer.readByte());
			} else if (opcode == 65) {
				definition.setNonExchangeItem(true);
			} else if (opcode == 78) {
				definition.setTertiaryMaleModel(buffer.readUShort());
			} else if (opcode == 79) {
				definition.setTertiaryFemaleModel(buffer.readUShort());
			} else if (opcode == 90) {
				definition.setMaleHead(buffer.readUShort());
			} else if (opcode == 91) {
				definition.setFemaleHead(buffer.readUShort());
			} else if (opcode == 92) {
				definition.setSecondaryMaleHead(buffer.readUShort());
			} else if (opcode == 93) {
				definition.setSecondaryFemaleHead(buffer.readUShort());
			} else if (opcode == 95) {
				definition.setModelYaw(buffer.readUShort());
			} else if (opcode == 97) {
				definition.setNoteInfoId(buffer.readUShort());
			} else if (opcode == 98) {
				definition.setNoteGraphicId(buffer.readUShort());
			} else if (opcode >= 100 && opcode < 110) {
				if (definition.getStackIds() == null) {
					definition.setStackIds(new int[10]);
					definition.setStackAmounts(new int[10]);
				}
				definition.getStackIds()[opcode - 100] = buffer.readUShort();
				definition.getStackAmounts()[opcode - 100] = buffer.readUShort();
			} else if (opcode == 110) {
				definition.setScaleX(buffer.readUShort());
			} else if (opcode == 111) {
				definition.setScaleY(buffer.readUShort());
			} else if (opcode == 112) {
				definition.setScaleZ(buffer.readUShort());
			} else if (opcode == 113) {
				definition.setAmbience(buffer.readSignedByte());
			} else if (opcode == 114) {
				definition.setContrast(buffer.readSignedByte());
			} else if (opcode == 115) {
				definition.setTeam(buffer.readUByte());
			} else if (opcode == 139) {
				definition.setLoanInfo(buffer.readUShort());
			} else if (opcode == 140) {
				definition.setLoanGraphicId(buffer.readUShort());
			} else if (opcode == 148) {
				definition.setPlaceHolderInfo(buffer.readUShort());
			} else if (opcode == 149) {
				definition.setPlaceHolderTemplate(buffer.readUShort());
			} else if (opcode == 249) {
				final int size = buffer.readUByte();
				definition.setParameters(new Int2ObjectArrayMap<>(size));
				for (int index = 0; index < size; index++) {
					final boolean stringInstance = buffer.readUByte() == 1;
					final int key = buffer.readUMedInt();
					final Object value = stringInstance ? buffer.readString() : buffer.readInt();
					definition.getParameters().put(key, value);
				}
			}

		}
	}
}