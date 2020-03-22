package org.apollo.cache.decoder.rsenum;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apollo.cache.Archive;
import org.apollo.cache.Cache;
import org.apollo.cache.CacheBuffer;
import org.apollo.cache.RSFile;
import org.apollo.cache.def.EnumDefinition;

/**
 * Decodes archive two folder eight into {@link EnumDefinition}s.
 *
 * @author Cjay0091
 */
public final class EnumDefinitionDecoder implements Runnable {

	/**
	 * The  IndexedFileSystem.
	 */
	private final Cache cache;

	/**
	 * Creates the EnumDefinitionDecoder.
	 *
	 * @param cache The {@link Archive}.
	 */
	public EnumDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		final var fs = cache.getArchive(2);
		final var folder = fs.findFolderByID(8);

		EnumDefinition[] definitions = new EnumDefinition[folder.getHighestId() + 1];
		for (RSFile file : folder.getRSFiles()) {
			definitions[file.getID()] = decode(file.getID(), file.getData());
		}

		EnumDefinition.init(definitions);
	}

	/**
	 * Decodes a single definition.
	 *
	 * @param id     The item's id.
	 * @param buffer The buffer.
	 * @return The {@link EnumDefinition}.
	 */
	private EnumDefinition decode(int id, CacheBuffer buffer) {
		EnumDefinition def = new EnumDefinition(id);
		for (; ; ) {
			int opcode = buffer.readUByte();
			if (opcode == 0) {
				return def;
			} else if (opcode == 1) {
				def.setKeyType(ScriptVarType.forCharKey((char) buffer.readUByte()));
			} else if (opcode == 2) {
				def.setValType(ScriptVarType.forCharKey((char) buffer.readUByte()));
			} else if (opcode == 3) {
				def.setDefaultString(buffer.readString());
			} else if (opcode == 4) {
				def.setDefaultInt(buffer.readInt());
			} else if (opcode == 5) {
				final var size = buffer.readUShort();
				final var values = new Int2ObjectOpenHashMap<String>(size);
				for (int index = 0; index < size; index++) {
					values.put(buffer.readInt(), buffer.readString());
				}
				def.setStringValues(values);
			} else if (opcode == 6) {
				final var size = buffer.readUShort();
				final var values = new Int2IntOpenHashMap(size);
				for (int index = 0; index < size; index++) {
					values.put(buffer.readInt(), buffer.readInt());
				}
				def.setIntValues(values);
			}
		}
	}
}