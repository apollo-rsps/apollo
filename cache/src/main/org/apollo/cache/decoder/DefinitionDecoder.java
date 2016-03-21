package org.apollo.cache.decoder;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.archive.Archive;
import org.apollo.cache.def.Definition;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

/**
 * Decodes data from the {@code obj.dat} file into {@link Definition}s.
 *
 * @author AymericDu
 */
public abstract class DefinitionDecoder<D extends Definition> implements Runnable {

	/**
	 * The  IndexedFileSystem.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the DefinitionDecoder.
	 *
	 * @param fs The {@link IndexedFileSystem}.
	 */
	public DefinitionDecoder(IndexedFileSystem fs) {
		this.fs = fs;
	}

	@Override
	public void run() {
		try {
			Archive config = fs.getArchive(0, 2);
			ByteBuffer data = config.getEntry(this.getNameFile() + ".dat").getBuffer();
			ByteBuffer idx = config.getEntry(this.getNameFile() + ".idx").getBuffer();

			int count = idx.getShort(), index = 2;
			int[] indices = new int[count];
			for (int i = 0; i < count; i++) {
				indices[i] = index;
				index += idx.getShort();
			}

			this.initDefinition(count, data, indices);
		} catch (IOException e) {
			throw new UncheckedIOException("Error decoding Definitions.", e);
		}
	}

	/**
	 * @return the file name of files .dat and .idx
     */
	protected abstract String getNameFile();

	protected abstract void initDefinition(int count, ByteBuffer data, int[] indices);

	/**
	 * Decodes a single definition.
	 *
	 * @param id The definition's id.
	 * @param data The buffer.
	 * @return The {@link Definition}.
	 */
	protected abstract D decode(int id, ByteBuffer data);
}
