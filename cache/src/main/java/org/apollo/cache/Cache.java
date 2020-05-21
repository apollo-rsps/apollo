package org.apollo.cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Path;


public class Cache {

	private static final String DATA_FILE = "main_file_cache.dat2";
	private static final String INDEX_FILE = "main_file_cache.idx";

	private Index[] indexes;
	private Index masterIndex;
	private Archive[] archives;
	private int[] crcs;

	private Cache(String path) {
		openFiles(path);
	}

	@SuppressWarnings("resource")
	private final void openFiles(String path) {
		try {
			String seperator = System.getProperty("file.separator", "/");

			RandomAccessFile dataFile = new RandomAccessFile(path + seperator + DATA_FILE, "rw");

			RandomAccessFile referenceFile = new RandomAccessFile(path + seperator + INDEX_FILE + "255", "rw");
			masterIndex = new Index(255, dataFile.getChannel(), referenceFile.getChannel(), 0x20000000);

			int numIndices = masterIndex.getFileCount();
			indexes = new Index[numIndices];
			archives = new Archive[numIndices];
			for (int i = 0; i < numIndices; i++) {
				RandomAccessFile indexFile = new RandomAccessFile(path + seperator + INDEX_FILE + i, "rw");
				indexes[i] = new Index(i, dataFile.getChannel(), indexFile.getChannel(), 0x10000000);
				if (masterIndex.get(i) != null) archives[i] = new Archive(i, this);
			}

			crcs = new int[numIndices];
			for (int i = 0; i < archives.length; i++) {
				var archive = getArchive(i);
				crcs[i] = archive == null ? 0 : archive.getCRC32();
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	/**
	 * Finishing all pending operations.
	 */
	public void flush() {
		if (archives == null) throw new RuntimeException("Cache is closed.");
		for (int i = 0; i < archives.length; i++)
			if (archives[i] != null && archives[i].isLoaded()) archives[i].finish();
	}

	/**
	 * Finishing all pending operations then
	 * Closes cache and disposes all data.
	 * Calling close() on closed cache has no effect.
	 */
	public void close() {
		if (indexes == null) return; // closed cache
		flush();
		for (int i = 0; i < indexes.length; i++)
			indexes[i].close();
		masterIndex.close();

		archives = null;
		indexes = null;
		masterIndex = null;
	}

	/**
	 * Closes cache without finishing all pending operations.
	 */
	public void closeDiscard() {
		if (indexes == null) return; // closed cache

		for (int i = 0; i < indexes.length; i++)
			indexes[i].close();
		masterIndex.close();

		archives = null;
		indexes = null;
		masterIndex = null;
	}


	/**
	 * Opens cache at given path.
	 *
	 * @param path Folder where cache is located.
	 * @return Opened cache or null if something failed.
	 */
	public static Cache openCache(String path) {
		return new Cache(path);
	}

	public static Cache openCache(Path path) {
		return new Cache(path.toString());
	}


	/**
	 * Create's new cache on given path.
	 *
	 * @param path         Folder where to create new cache.
	 * @param indicesCount Count of indices , can't be lower than 0 or higher than 254.
	 * @return Created cache or null if something failed.
	 */
	@SuppressWarnings("resource")
	public static Cache createNewCache(String path, int indicesCount) {
		if (indicesCount < 0 || indicesCount > 254) return null;

		File file = new File(path);
		if (file.isFile()) return null;

		if (!file.exists()) file.mkdirs();

		if (!file.isDirectory()) return null;

		try {
			String seperator = System.getProperty("file.separator", "/");
			File data = new File(path + seperator + DATA_FILE);
			if (data.exists() || !data.createNewFile() || !data.canWrite() || !data.canRead()) return null;

			for (int i = 0; i < indicesCount; i++) {
				File index = new File(path + seperator + INDEX_FILE + i);
				if (index.exists() || !index.createNewFile() || !index.canWrite() || !data.canRead()) return null;
			}

			File index255 = new File(path + seperator + INDEX_FILE + "255");
			if (index255.exists() || !index255.createNewFile() || !index255.canWrite() || !index255.canRead()) {
				return null;
			}

			RandomAccessFile dataFile = new RandomAccessFile(path + seperator + DATA_FILE, "rw");
			RandomAccessFile referenceFile = new RandomAccessFile(path + seperator + INDEX_FILE + "255", "rw");
			Index store_255 = new Index(255, dataFile.getChannel(), referenceFile.getChannel(), 0x7a120);

			for (int i = 0; i < indicesCount; i++) {
				byte[] pdata = Helper.encodeFITContainer(new byte[]{5, 0, 0, 0},
						0); // empty fs (protocol 5, props 0, folders count - 0)
				store_255.put(i, new CacheBuffer(pdata), pdata.length);
			}

			dataFile.getChannel().close();
			referenceFile.getChannel().close();
			return new Cache(path);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}


	/**
	 * Get's specific file store.
	 * Returns null if store is not available in this cache.
	 * Note:Using this method to get information store (idx255) results in
	 * null return, use getInformationStore() instead.
	 */
	public Index getIndex(int id) {
		if (indexes == null) throw new RuntimeException("Cache is closed.");
		if (id < 0 || id >= indexes.length) return null;
		return indexes[id];
	}

	/**
	 * Get's specific file system.
	 * If it's not loaded then it loads it.
	 */
	public Archive getArchive(int id) {
		if (archives == null) throw new RuntimeException("Cache is closed.");
		if (id < 0 || id >= archives.length || archives[id] == null) return null;
		Archive system = archives[id];
		if (!system.isLoaded()) system.load();
		return system;
	}

	/**
	 * Get's information about files store.
	 */
	public Index getMasterIndex() {
		if (masterIndex == null) throw new RuntimeException("Cache is closed.");
		return masterIndex;
	}

	/**
	 * Get's count of indices in this cache.
	 */
	public int getIndicesCount() {
		if (indexes == null) throw new RuntimeException("Cache is closed.");
		return indexes.length;
	}

	/**
	 * Get's an array of all index CRCs.
	 */
	public int[] getCrcs() {
		if (indexes == null) throw new RuntimeException("Cache is closed.");
		return crcs;
	}

	public CacheBuffer generateInformationStoreDescriptor(boolean whirlpool) {
		if (archives == null)
			throw new RuntimeException("Cache is closed.");
		for (int i = 0; i < archives.length; i++)
			if (!archives[i].isLoaded())
				archives[i].load();
		flush();
		int indicesCount = getIndicesCount();
		CacheBuffer alloc = new CacheBuffer(whirlpool ? ((1 + (indicesCount * 72) + 1 + 64) * 10) : (256 * 8));
		if (whirlpool)
			alloc.writeByte(indicesCount);
		for (int i = 0; i < (whirlpool ? indicesCount : 256); i++) {
			if (i >= archives.length) {
				alloc.writeInt(0xCAFEBABE);
				alloc.writeInt(0xBEEFBEEF);
				continue;
			}
			alloc.writeInt(archives[i].getCRC32());
			alloc.writeInt(archives[i].getVersion());
			if (whirlpool)
				alloc.writeBytes(archives[i].getDigest(), 0, 64);
		}
		if (whirlpool) {
			byte[] selfDigest = Whirlpool.whirlpool(alloc.getBuffer(), 0, alloc.getPosition());
			CacheBuffer rsa = new CacheBuffer(65);
			rsa.writeByte(10);
			rsa.writeBytes(selfDigest, 0, 64);
			BigInteger data = new BigInteger(rsa.getBuffer());
			byte[] encrypted = data.toByteArray();
			alloc.writeBytes(encrypted, 0, encrypted.length);
		}
		return Helper.encodeFITContainer(new CacheBuffer(alloc.toArray(0, alloc.getPosition())), 0, Helper.COMPRESSION_NONE);
	}

	@Override
	public void finalize() {
		if (indexes != null) {
			System.err.println("mgi.tools.jagcached.cache:Cache not closed.");
			close();
		}
	}
}
