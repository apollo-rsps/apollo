package org.apollo.fs;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

/**
 * A file system based on top of the operating system's file system. It consists of a data file and index files. Index
 * files point to blocks in the data file, which contains the actual data.
 * 
 * @author Graham
 */
public final class IndexedFileSystem implements Closeable {

	/**
	 * The cached CRC table.
	 */
	private ByteBuffer crcTable;

	/**
	 * The data file.
	 */
	private RandomAccessFile data;

	/**
	 * The index files.
	 */
	private RandomAccessFile[] indices = new RandomAccessFile[256];

	/**
	 * Read only flag.
	 */
	private final boolean readOnly;

	/**
	 * Creates the file system with the specified base directory.
	 * 
	 * @param base The base directory.
	 * @param readOnly Indicates whether the file system will be read only or not.
	 * @throws FileNotFoundException If the data files could not be found.
	 */
	public IndexedFileSystem(Path base, boolean readOnly) throws FileNotFoundException {
		this.readOnly = readOnly;
		detectLayout(base);
	}

	@Override
	public void close() throws IOException {
		if (data != null) {
			synchronized (data) {
				data.close();
			}
		}

		for (RandomAccessFile index : indices) {
			if (index != null) {
				synchronized (index) {
					index.close();
				}
			}
		}
	}

	/**
	 * Automatically detect the layout of the specified directory.
	 * 
	 * @param base The base directory.
	 * @throws FileNotFoundException If the data files could not be found.
	 */
	private void detectLayout(Path base) throws FileNotFoundException {
		int indexCount = 0;
		for (int index = 0; index < indices.length; index++) {
			Path idx = base.resolve("main_file_cache.idx" + index);
			if (Files.exists(idx) && !Files.isDirectory(idx)) {
				indexCount++;
				indices[index] = new RandomAccessFile(idx.toFile(), readOnly ? "r" : "rw");
			}
		}
		if (indexCount <= 0) {
			throw new FileNotFoundException("No index file(s) present.");
		}

		Path resources = base.resolve("main_file_cache.dat");
		if (Files.exists(resources) && !Files.isDirectory(resources)) {
			data = new RandomAccessFile(resources.toFile(), readOnly ? "r" : "rw");
		} else {
			throw new FileNotFoundException("No data file present.");
		}
	}

	/**
	 * Gets the CRC table.
	 * 
	 * @return The CRC table.
	 * @throws IOException If an I/O error occurs.
	 */
	public ByteBuffer getCrcTable() throws IOException {
		if (readOnly) {
			synchronized (this) {
				if (crcTable != null) {
					return crcTable.duplicate();
				}
			}

			int archives = getFileCount(0);
			int hash = 1234;
			int[] crcs = new int[archives];

			// calculate the CRCs
			CRC32 crc32 = new CRC32();
			for (int i = 1; i < crcs.length; i++) {
				crc32.reset();

				ByteBuffer buffer = getFile(0, i);
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes, 0, bytes.length);
				crc32.update(bytes, 0, bytes.length);

				crcs[i] = (int) crc32.getValue();
			}

			// hash the CRCs and place them in the buffer
			ByteBuffer buffer = ByteBuffer.allocate(crcs.length * 4 + 4);
			for (int crc : crcs) {
				hash = (hash << 1) + crc;
				buffer.putInt(crc);
			}

			buffer.putInt(hash);
			buffer.flip();

			synchronized (this) {
				crcTable = buffer.asReadOnlyBuffer();
				return crcTable.duplicate();
			}
		}
		throw new IOException("Cannot get CRC table from a writable file system.");
	}

	/**
	 * Gets a file.
	 * 
	 * @param descriptor The {@link FileDescriptor} which points to the file.
	 * @return A {@link ByteBuffer} which contains the contents of the file.
	 * @throws IOException If an I/O error occurs.
	 */
	public ByteBuffer getFile(FileDescriptor descriptor) throws IOException {
		Index index = getIndex(descriptor);
		ByteBuffer buffer = ByteBuffer.allocate(index.getSize());

		// calculate some initial values
		long ptr = index.getBlock() * FileSystemConstants.BLOCK_SIZE;
		int read = 0;
		int size = index.getSize();
		int blocks = size / FileSystemConstants.CHUNK_SIZE;
		if (size % FileSystemConstants.CHUNK_SIZE != 0) {
			blocks++;
		}

		for (int i = 0; i < blocks; i++) {
			// read header
			byte[] header = new byte[FileSystemConstants.HEADER_SIZE];
			synchronized (data) {
				data.seek(ptr);
				data.readFully(header);
			}

			// increment pointers
			ptr += FileSystemConstants.HEADER_SIZE;

			// parse header
			int nextFile = (header[0] & 0xFF) << 8 | header[1] & 0xFF;
			int curChunk = (header[2] & 0xFF) << 8 | header[3] & 0xFF;
			int nextBlock = (header[4] & 0xFF) << 16 | (header[5] & 0xFF) << 8 | header[6] & 0xFF;
			int nextType = header[7] & 0xFF;

			// check expected chunk id is correct
			if (i != curChunk) {
				throw new IOException("Chunk id mismatch.");
			}

			// calculate how much we can read
			int chunkSize = size - read;
			if (chunkSize > FileSystemConstants.CHUNK_SIZE) {
				chunkSize = FileSystemConstants.CHUNK_SIZE;
			}

			// read the next chunk and put it in the buffer
			byte[] chunk = new byte[chunkSize];
			synchronized (data) {
				data.seek(ptr);
				data.readFully(chunk);
			}
			buffer.put(chunk);

			// increment pointers
			read += chunkSize;
			ptr = (long) nextBlock * (long) FileSystemConstants.BLOCK_SIZE;

			// if we still have more data to read, check the validity of the header
			if (size > read) {
				if (nextType != descriptor.getType() + 1) {
					throw new IOException("File type mismatch.");
				}

				if (nextFile != descriptor.getFile()) {
					throw new IOException("File id mismatch.");
				}
			}
		}

		buffer.flip();
		return buffer;
	}

	/**
	 * Gets a file.
	 * 
	 * @param type The file type.
	 * @param file The file id.
	 * @return A {@link ByteBuffer} which contains the contents of the file.
	 * @throws IOException If an I/O error occurs.
	 */
	public ByteBuffer getFile(int type, int file) throws IOException {
		return getFile(new FileDescriptor(type, file));
	}

	/**
	 * Gets the number of files with the specified type.
	 * 
	 * @param type The type.
	 * @return The number of files.
	 * @throws IOException If an I/O error occurs.
	 */
	private int getFileCount(int type) throws IOException {
		if (type < 0 || type >= indices.length) {
			throw new IndexOutOfBoundsException("File type out of bounds.");
		}

		RandomAccessFile indexFile = indices[type];
		synchronized (indexFile) {
			return (int) (indexFile.length() / FileSystemConstants.INDEX_SIZE);
		}
	}

	/**
	 * Gets the index of a file.
	 * 
	 * @param descriptor The {@link FileDescriptor} which points to the file.
	 * @return The {@link Index}.
	 * @throws IOException If an I/O error occurs.
	 */
	private Index getIndex(FileDescriptor descriptor) throws IOException {
		int index = descriptor.getType();
		if (index < 0 || index >= indices.length) {
			throw new IndexOutOfBoundsException("File descriptor type out of bounds.");
		}

		byte[] buffer = new byte[FileSystemConstants.INDEX_SIZE];
		RandomAccessFile indexFile = indices[index];
		synchronized (indexFile) {
			long ptr = descriptor.getFile() * FileSystemConstants.INDEX_SIZE;
			if (ptr >= 0 && indexFile.length() >= ptr + FileSystemConstants.INDEX_SIZE) {
				indexFile.seek(ptr);
				indexFile.readFully(buffer);
			} else {
				throw new FileNotFoundException("Could not find find index.");
			}
		}

		return Index.decode(buffer);
	}

	/**
	 * Checks if this {@link IndexedFileSystem} is read only.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

}