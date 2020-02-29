package org.apollo.cache;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.util.Comparator;

public class Group {

	/**
	 * Contains ID of the folder.
	 */
	private int id;
	/**
	 * Contains name hash of this folder.
	 */
	private int name;
	/**
	 * Contains version of packed folder file.
	 */
	private int version;

	/**
	 * Contains crc of packed folder file.
	 */
	private int crc;
	/**
	 * Contains 64-byte whirlpool digest.
	 */
	private byte[] digest;

	private Int2ObjectLinkedOpenHashMap<RSFile> files;
	private Int2IntOpenHashMap filesByName;

	/**
	 * Contains packed files buffer
	 * which was provided with load() call.
	 */
	private CacheBuffer packedFiles;
	/**
	 * Contains xtea keys. (int[4])
	 * Can be null.
	 */
	private int[] xtea;
	/**
	 * Wheter any changes were made.
	 */
	private boolean needsRepack;
	/**
	 * Wheter any of those things changed (id,name,version,crc,digest,files and their data).
	 */
	private boolean fileSystemInfoChanged;

	/**
	 * Constructor for copy() methods.
	 */
	private Group() {

	}

	/**
	 * Create's new folder with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(Int2ObjectLinkedOpenHashMap<RSFile> files) {
		this(-1, -1, 0, files);
	}

	/**
	 * Create's new folder with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(String name, int version, Int2ObjectLinkedOpenHashMap<RSFile> files) {
		this(-1, Helper.strToI(name), version, files);
	}

	/**
	 * Create's new folder with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int name, int version, Int2ObjectLinkedOpenHashMap<RSFile> files) {
		this(-1, name, version, files);
	}

	/**
	 * Create's new folder from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int folderID, String name, int version, Int2ObjectLinkedOpenHashMap<RSFile> files) {
		this(folderID, Helper.strToI(name), version, files);
	}

	/**
	 * Create's new folder from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int folderID, int name, int version, Int2ObjectLinkedOpenHashMap<RSFile> files) {
		this.id = folderID;
		this.name = name;
		this.version = version;
		this.files = files;
		for (var file : files.values()) {
			if (file.getID() != -1) {
				continue;
			}
			file.setID(getRSFreeFileID());
		}
		this.filesByName = new Int2IntOpenHashMap();
		updateHashes(this.packedFiles = pack());
	}

	/**
	 * Create's new unloaded folder from given FIT data.
	 */
	public Group(int folderID, int name, int version, int crc32, byte[] digest, int[] filesIDS, int[] filesNames) {
		if (digest.length != 64 || filesIDS.length != filesNames.length) {
			throw new RuntimeException("Invalid data provided.");
		}
		this.id = folderID;
		this.name = name;
		this.version = version;
		this.crc = crc32;
		this.digest = digest;
		this.files = new Int2ObjectLinkedOpenHashMap<>(filesIDS.length);//[filesIDS.length];
		this.filesByName = new Int2IntOpenHashMap();
		for (int i = 0; i < filesIDS.length; i++) {
			var file = new RSFile(filesIDS[i], filesNames[i]);
			files.put(file.getID(), file);
			if (file.hasName()) {
				filesByName.put(file.getName(), file.getID());
			}
		}
	}

	/**
	 * Copy's this folder and every file in it.
	 */
	public Group copy() {
		Group copy = new Group();
		copy.id = id;
		copy.name = name;
		copy.version = version;
		copy.crc = crc;
		if (digest != null) {
			copy.digest = new byte[64];
			System.arraycopy(digest, 0, copy.digest, 0, 64);
		}
		if (files != null) {
			copy.files = new Int2ObjectLinkedOpenHashMap<>(files.size());
			for (var file : files.values()) {
				copy.files.put(file.getID(), file.copy());
			}
		}
		if (filesByName != null) {
			copy.filesByName = new Int2IntOpenHashMap();
			for (int file : filesByName.values()) {
				copy.filesByName.put(file, filesByName.get(file));
			}
		}
		if (packedFiles != null) {
			copy.packedFiles = new CacheBuffer(packedFiles.toArray(0, packedFiles.getBuffer().length),
					packedFiles.getPosition());
		}
		if (xtea != null) {
			copy.xtea = new int[4];
			System.arraycopy(xtea, 0, copy.xtea, 0, 4);
		}
		copy.needsRepack = needsRepack;
		copy.fileSystemInfoChanged = fileSystemInfoChanged;
		return copy;
	}


	/**
	 * Load's packed folder file.
	 */
	public void load(CacheBuffer packedFiles) {
		load(packedFiles, null);
	}

	/**
	 * Unload's this folder , can be called
	 * only by filesystem unloadCachedData() method.
	 */
	public void unload() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded folder.");
		packedFiles = null;
		for (var file : files.values()) {
			file.unload();
		}
	}

	/**
	 * Load's packed folder file.
	 */
	public void load(CacheBuffer packedFiles, int[] xtea) {
		if (isLoaded()) throw new RuntimeException("Already loaded.");
		this.packedFiles = packedFiles;
		this.xtea = xtea;
		unpack(packedFiles);
	}


	/**
	 * Finishe's any changes by generating new packedFiles buffer and updating hashes
	 * if something was changed.
	 */
	public CacheBuffer finish() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");

		if (needsRepack()) {
			//sortFiles();
			packedFiles = pack();
			updateHashes(packedFiles);
			markAsNotNeedRepack();
			return packedFiles;
		}
		return packedFiles;
	}

	/**
	 * Unpack's given packed files buffer.
	 */
	@SuppressWarnings("Duplicates")
	private void unpack(CacheBuffer packed) {
		if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
			Helper.decryptContainer(packed, xtea);
		}

		packed = Helper.decodeFilesContainer(packed);

		if (filesCount() <= 1) {
			if (filesCount() <= 0) return;
			CacheBuffer copy = new CacheBuffer(packed.getBuffer().length);
			copy.writeBytes(packed.getBuffer(), 0, packed.getBuffer().length);
			files.get(files.firstIntKey()).load(copy);
			return;
		}

		int sectorsCount = packed.getBuffer()[packed.getBuffer().length - 1] & 0xFF;
		int[] lengths = new int[filesCount()];
		packed.setPosition(packed.getBuffer().length - ((sectorsCount * filesCount() * 4) + 1));
		for (int sectorID = 0; sectorID < sectorsCount; sectorID++) {
			for (int i = 0, length = 0; i < filesCount(); i++) {
				lengths[i] += (length += packed.readInt());
			}
		}
		int index = 0;
		var sortedFiles = files.values().parallelStream().sorted(Comparator.comparingInt(RSFile::getID))
				.toArray(RSFile[]::new);
		for (var file : sortedFiles) {
			file.load(new CacheBuffer(lengths[index]));
			lengths[index++] = 0;
		}
		packed.setPosition(packed.getBuffer().length - ((sectorsCount * filesCount() * 4) + 1));
		for (int fRead = 0, sectorID = 0; sectorID < sectorsCount; sectorID++) {
			int length = 0;
			int counter = 0;
			for (var file : sortedFiles) {
				length += packed.readInt();
				System.arraycopy(packed.getBuffer(), fRead, file.getData().getBuffer(), lengths[counter], length);
				lengths[counter++] += length;
				fRead += length;
			}
		}

	}

	/**
	 * Pack's unpacked files.
	 */
	private CacheBuffer pack() {
		if (filesCount() <= 1) {
			if (filesCount() <= 0) {
				CacheBuffer container = Helper.encodeFilesContainer(new CacheBuffer(new byte[0]), version);
				if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
					Helper.encryptContainer(container, xtea);
				}
				return container.trim();
			}
			var firstFile = files.get(files.firstIntKey());
			CacheBuffer packed = new CacheBuffer(firstFile.getData().getBuffer().length);
			packed.writeBytes(firstFile.getData().getBuffer(), 0, firstFile.getData().getBuffer().length);
			CacheBuffer container = Helper.encodeFilesContainer(packed, version);
			if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
				Helper.encryptContainer(container, xtea);
			}
			return container.trim();
		}
		var sortedFiles = files.values().parallelStream().sorted(Comparator.comparingInt(RSFile::getID))
				.toArray(RSFile[]::new);
		int allocLength = 1 + (4 * filesCount()); // sector header
		for (var file : sortedFiles)
			allocLength += file.getData().getBuffer().length;
		CacheBuffer packed = new CacheBuffer(allocLength);
		for (var file : sortedFiles) {
			packed.writeBytes(file.getData().getBuffer(), 0, file.getData().getBuffer().length);
		}

		var lengthOffset = 0;
		for (var file : sortedFiles) {
			var length = file.getData().getBuffer().length;
			packed.writeInt(length - lengthOffset);
			lengthOffset += length - lengthOffset;
		}
		packed.writeByte(1); // 1 sector
		CacheBuffer container = Helper.encodeFilesContainer(packed, version);
		if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
			Helper.encryptContainer(container, xtea);
		}
		return container.trim();
	}

	/**
	 * Update's crc32 and whirlpool hash to one's from
	 * given buffer.
	 */
	private void updateHashes(CacheBuffer packedFiles) {
		crc = Helper.crc32(packedFiles, 0, packedFiles.getBuffer().length - 2);
		digest = Whirlpool.whirlpool(packedFiles.getBuffer(), 0, packedFiles.getBuffer().length - 2);
	}


	/**
	 * Add's new file to this folder.
	 * If there's already a file with same id then
	 * it get's overwriten.
	 */
	public void addRSFile(RSFile file) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		if (file.getID() == -1) file.setID(getRSFreeFileID());
		files.put(file.getID(), file);
		if (file.hasName()) {
			filesByName.put(file.getName(), file.getID());
		}
		needsRepack = fileSystemInfoChanged = true;
	}

	/**
	 * Remove's file from this folder.
	 */
	public void removeFile(RSFile file) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		files.remove(file.getID());
		filesByName.remove(file.getName());
		needsRepack = fileSystemInfoChanged = true;
	}

	/**
	 * Delete's all files on this folder.
	 */
	public void deleteAllFiles() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		files.clear();
		needsRepack = fileSystemInfoChanged = true;
	}

	/**
	 * Recalculate's folder's hashes.
	 */
	public void recalculate() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		updateHashes(packedFiles);
		fileSystemInfoChanged = true;
	}

	/**
	 * Returns highest file ids.
	 */
	public int getHighestId() {
		return getRSFreeFileID() - 1;
	}


	/**
	 * Get's free file ID.
	 */
	private int getRSFreeFileID() {
		if (files.isEmpty()) {
			return 0;
		}

		int highest = -1;
		for (var file : files.values()) {
			if (file.getID() > highest) {
				highest = file.getID();
			}
		}
		return highest + 1;
	}

	/**
	 * Find's file by id's id.
	 * Returns null if not found.
	 */
	public RSFile findRSFileByID(int id) {
		if (!isLoaded()) throw new RuntimeException("Folder is not loaded.");
		return files.get(id);
	}

	/**
	 * Find's file by name.
	 * Returns null if not found.
	 */
	public RSFile findRSFileByName(String name) {
		return findRSFileByName(Helper.strToI(name));
	}

	/**
	 * Find's file by name.
	 * Returns null if not found.
	 */
	public RSFile findRSFileByName(int name) {
		if (!isLoaded()) throw new RuntimeException("Folder is not loaded.");

		if (name == -1) return null;

		return files.get(filesByName.get(name));
	}

	/**
	 * Wheter this folder was loaded.
	 */
	public boolean isLoaded() {
		return packedFiles != null;
	}

	/**
	 * Wheter this folder needs repacking.
	 */
	private boolean needsRepack() {
		if (needsRepack) return true;
		for (var file : files.values()) {
			if (file.isDataChanged()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Mark's this folder as already repacked.
	 */
	private void markAsNotNeedRepack() {
		needsRepack = false;
		for (var file : files.values()) {
			file.markDataAsNotChanged();
		}
	}


	/**
	 * Wheter this folder was changed in any way.
	 */
	public boolean isFileSystemInfoChanged() {
		if (fileSystemInfoChanged) return true;
		for (var file : files.values()) {
			if (file.isFileSystemInfoChanged()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Mark's this folder as not changed, this
	 * happens when fit is repacked.
	 */
	public void markFileSystemInfoAsNotChanged() {
		fileSystemInfoChanged = false;
		for (var file : files.values()) {
			file.markFileSystemInfoAsNotChanged();
		}
	}

	/**
	 * Mark's this folder file system info as changed,
	 * get's called by FS only.
	 */
	public void markFileSystemInfoAsChanged() {
		fileSystemInfoChanged = true;
	}

	/**
	 * Get's count of files this folder has.
	 */
	public int filesCount() {
		return files.size(); //files.keySet().stream().max(Comparator.naturalOrder()).orElse(-1) + 1;
	}

	/**
	 * Get's all files that this folder has.
	 * Modifying the array in any way is not allowed.
	 *
	 * @return
	 */
	public ObjectCollection<RSFile> getRSFiles() {
		return files.values();
	}

	/**
	 * Get's ID of this folder.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Set's ID of this folder.
	 */
	public void setID(int id) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		if (this.id != id) this.fileSystemInfoChanged = true;
		this.id = id;
	}

	/**
	 * Get's name of this folder.
	 */
	public int getName() {
		return name;
	}

	/**
	 * Set's name of this folder.
	 */
	public void setName(String name) {
		setName(Helper.strToI(name));
	}

	/**
	 * Set's name of this folder.
	 */
	public void setName(int name) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		if (this.name != name) this.fileSystemInfoChanged = true;
		this.name = name;
	}

	/**
	 * Get's version of this folder.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Set's version of this folder.
	 */
	public void setVersion(int version) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		if (this.version != version) this.fileSystemInfoChanged = this.needsRepack = true;
		this.version = version;
	}

	/**
	 * Set's XTEA keys of this folder.
	 */
	public void setXTEA(int[] xtea) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded folder.");
		if (this.xtea != xtea) this.fileSystemInfoChanged = this.needsRepack = true;
		this.xtea = xtea;
	}


	/**
	 * Get's crc32 of packed version of this folder.
	 */
	public int getCRC32() {
		return crc;
	}

	/**
	 * Get's whirlpool digest of packed version of this folder.
	 */
	public byte[] getDigest() {
		return digest;
	}

	/**
	 * Get's packed files buffer.
	 * Call to finish() is a must if you want
	 * to get up-to-date version.
	 */
	public CacheBuffer getPackedFiles() {
		return packedFiles;
	}

	public boolean hasName() {
		return name != -1;
	}
}
