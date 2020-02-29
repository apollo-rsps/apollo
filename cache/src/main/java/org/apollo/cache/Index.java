package org.apollo.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Manages the reading and writing for a particular file store in the cache.
 */
public class Index {

    private static final int IDX_BLOCK_LEN = 6;
    private static final int HEADER_LEN = 8;
    private static final int EXPANDED_HEADER_LEN = 10;
    private static final int BLOCK_LEN = 512;
    private static final int EXPANDED_BLOCK_LEN = 510;
    private static final int TOTAL_BLOCK_LEN = HEADER_LEN + BLOCK_LEN;
    private static ByteBuffer tempBuffer = ByteBuffer.allocateDirect(TOTAL_BLOCK_LEN);

    private int index;
    private FileChannel indexChannel;
    private FileChannel dataChannel;
    private int maxSize;

    /**
     * Creates a new FileStore object.
     *
     * @param index        The index of this file store.
     * @param dataChannel  The channel of the data file for this file store.
     * @param indexChannel The channel of the index file for this file store.
     * @param maxSize      The maximum size of a file in this file store.
     */
    public Index(int index, FileChannel dataChannel,
				 FileChannel indexChannel, int maxSize) {
        this.index = index;
        this.dataChannel = dataChannel;
        this.indexChannel = indexChannel;
        this.maxSize = maxSize;
    }

    /**
     * Gets the number of files stored in this file store.
     *
     * @return This file store's file count.
     */
    public int getFileCount() {
        try {
            return (int) (indexChannel.size() / IDX_BLOCK_LEN);
        } catch (IOException ex) {
            return 0;
        }
    }

    /**
     * Reads a file from the file store.
     *
     * @param file The file to read.
     * @return The file's data, or null if the file was invalid.
     */
    public CacheBuffer get(int file) {
    	synchronized (dataChannel) {
			try {
				if (file * IDX_BLOCK_LEN + IDX_BLOCK_LEN > indexChannel.size()) {
					return null;
				}

				tempBuffer.position(0).limit(IDX_BLOCK_LEN);
				indexChannel.read(tempBuffer, file * IDX_BLOCK_LEN);
				tempBuffer.flip();
				int size = getMediumInt(tempBuffer);
				int block = getMediumInt(tempBuffer);

				if (size < 0 || size > maxSize) {
					return null;
				}
				if (block <= 0 || block > dataChannel.size() / TOTAL_BLOCK_LEN) {
					return null;
				}

				ByteBuffer fileBuffer = ByteBuffer.allocate(size);
				int remaining = size;
				int chunk = 0;
				int blockLen = file <= 0xffff ? BLOCK_LEN : EXPANDED_BLOCK_LEN;
				int headerLen = file <= 0xffff ? HEADER_LEN : EXPANDED_HEADER_LEN;
				while (remaining > 0) {
					if (block == 0) {
						return null;
					}

					int blockSize = remaining > blockLen ? blockLen : remaining;
					tempBuffer.position(0).limit(blockSize + headerLen);
					int read = dataChannel.read(tempBuffer, (long) block * TOTAL_BLOCK_LEN);
					if (read == -1) {
						return null;
					}

					tempBuffer.flip();

					int currentFile, currentChunk, nextBlock, currentIndex;
					if (file <= 65535) {
						currentFile = tempBuffer.getShort() & 0xffff;
						currentChunk = tempBuffer.getShort() & 0xffff;
						nextBlock = getMediumInt(tempBuffer);
						currentIndex = tempBuffer.get() & 0xff;
					} else {
						currentFile = tempBuffer.getInt();
						currentChunk = tempBuffer.getShort() & 0xffff;
						nextBlock = getMediumInt(tempBuffer);
						currentIndex = tempBuffer.get() & 0xff;
					}

					if (file != currentFile || chunk != currentChunk || index != currentIndex) {
						return null;
					}
					if (nextBlock < 0 || nextBlock > dataChannel.size() / TOTAL_BLOCK_LEN) {
						return null;
					}

					fileBuffer.put(tempBuffer);
					remaining -= blockSize;
					block = nextBlock;
					chunk++;
				}

				fileBuffer.flip();
				return new CacheBuffer(fileBuffer.array());
			} catch (IOException ex) {
				return null;
			}
		}
    }

    /**
     * Writes an item to the file store.
     *
     * @param file The file to write.
     * @param data The file's data.
     * @param size The size of the file.
     * @return true if the file was written, false otherwise.
     */
    public boolean put(int file, CacheBuffer data, int size) {
        if (size < 0 || size > maxSize) {
            throw new IllegalArgumentException("File too big: " + index + "," + file + " size: " + size);
        }

        boolean success = put(file, ByteBuffer.wrap(data.getBuffer()), size, true);
        if (!success) {
            success = put(file, ByteBuffer.wrap(data.getBuffer()), size, false);
        }

        return success;
    }

    private boolean put(int file, ByteBuffer data, int size, boolean exists) {
        try {
            int block;
            if (exists) {
                if (file * IDX_BLOCK_LEN + IDX_BLOCK_LEN > indexChannel.size()) {
                    return false;
                }

                tempBuffer.position(0).limit(IDX_BLOCK_LEN);
                indexChannel.read(tempBuffer, file * IDX_BLOCK_LEN);
                tempBuffer.flip().position(3);
                block = getMediumInt(tempBuffer);

                if (block <= 0 || block > dataChannel.size() / TOTAL_BLOCK_LEN) {
                    return false;
                }
            } else {
                block = (int) ((dataChannel.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN);
                if (block == 0) {
                    block = 1;
                }
            }

            tempBuffer.position(0);
            putMediumInt(tempBuffer, size);
            putMediumInt(tempBuffer, block);
            tempBuffer.flip();
            indexChannel.write(tempBuffer, file * IDX_BLOCK_LEN);

            int remaining = size;
            int chunk = 0;
            int blockLen = file <= 0xffff ? BLOCK_LEN : EXPANDED_BLOCK_LEN;
            int headerLen = file <= 0xffff ? HEADER_LEN : EXPANDED_HEADER_LEN;
            while (remaining > 0) {
                int nextBlock = 0;
                if (exists) {
                    tempBuffer.position(0).limit(headerLen);
                    int read = dataChannel.read(tempBuffer, (long) block * TOTAL_BLOCK_LEN);
                    if (read == -1) {
                        return false;
                    }

                    tempBuffer.flip();

                    int currentFile, currentChunk, currentIndex;
                    if (file <= 0xffff) {
                        currentFile = tempBuffer.getShort() & 0xffff;
                        currentChunk = tempBuffer.getShort() & 0xffff;
                        nextBlock = getMediumInt(tempBuffer);
                        currentIndex = tempBuffer.get() & 0xff;
                    } else {
                        currentFile = tempBuffer.getInt();
                        currentChunk = tempBuffer.getShort() & 0xffff;
                        nextBlock = getMediumInt(tempBuffer);
                        currentIndex = tempBuffer.get() & 0xff;
                    }

                    if (file != currentFile || chunk != currentChunk || index != currentIndex) {
                        return false;
                    }
                    if (nextBlock < 0 || nextBlock > dataChannel.size() / TOTAL_BLOCK_LEN) {
                        return false;
                    }
                }

                if (nextBlock == 0) {
                    exists = false;
                    nextBlock = (int) ((dataChannel.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN);
                    if (nextBlock == 0) {
                        nextBlock = 1;
                    }
                    if (nextBlock == block) {
                        nextBlock++;
                    }
                }

                if (remaining <= blockLen) {
                    nextBlock = 0;
                }
                tempBuffer.position(0).limit(TOTAL_BLOCK_LEN);
                if (file <= 0xffff) {
                    tempBuffer.putShort((short) file);
                    tempBuffer.putShort((short) chunk);
                    putMediumInt(tempBuffer, nextBlock);
                    tempBuffer.put((byte) index);
                } else {
                    tempBuffer.putInt(file);
                    tempBuffer.putShort((short) chunk);
                    putMediumInt(tempBuffer, nextBlock);
                    tempBuffer.put((byte) index);
                }

                int blockSize = remaining > blockLen ? blockLen : remaining;
                data.limit(data.position() + blockSize);
                tempBuffer.put(data);
                tempBuffer.flip();

                dataChannel.write(tempBuffer, (long) block * TOTAL_BLOCK_LEN);
                remaining -= blockSize;
                block = nextBlock;
                chunk++;
            }

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private static int getMediumInt(ByteBuffer buffer) {
        return ((buffer.get() & 0xff) << 16) | ((buffer.get() & 0xff) << 8) |
               (buffer.get() & 0xff);
    }

    private static void putMediumInt(ByteBuffer buffer, int val) {
        buffer.put((byte) (val >> 16));
        buffer.put((byte) (val >> 8));
        buffer.put((byte) val);
    }

    /**
     * Close's this store.
     */
    public void close() {
        try {
            this.dataChannel.close();
        } catch (IOException e) {
        }
        try {
            this.indexChannel.close();
        } catch (IOException e) {
        }
    }
}
