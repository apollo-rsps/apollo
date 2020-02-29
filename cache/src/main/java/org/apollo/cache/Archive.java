package org.apollo.cache;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.util.Comparator;

public class Archive {

	/**
	 * Underlying cache.
	 */
	private Cache cache;
	/**
	 * Contains ID of the file system.
	 */
	private int id;

	/**
	 * Wheter to use automatic version incrementor.
	 */
	private boolean useAutomaticVersionIncremetor = true;

	/**
	 * Contains all groups by id.
	 */
	private Int2ObjectLinkedOpenHashMap<Group> folders;

	/**
	 * Contains all groups by name.
	 */
	private Int2IntOpenHashMap foldersByName;

	/**
	 * Wheter whirlpool hashes are used.
	 */
	private boolean useWhirlpool;
	/**
	 * Wheter names are used.
	 */
	private boolean useNames;

	/**
	 * Whether compression lenths are used.
	 */
	private boolean useCompressionLengths;

	/**
	 * Whether flag8 is used.
	 */
	private boolean useFlag8;

	/**
	 * Version of this file system.
	 */
	private int version;
	/**
	 * CRC32 of this file system.
	 */
	private int crc;
	/**
	 * 64byte whirlpool digest
	 * of this file system.
	 */
	private byte[] digest;

	/**
	 * Contains packed filesystem file
	 * which was provided with load() call.
	 */
	private CacheBuffer packed;
	/**
	 * Wheter any changes were made to
	 * file system properties such as version or properties.
	 */
	private boolean changed;

	private int protocol = 7;

	public Archive(int id, Cache cache) {
		this.id = id;
		this.cache = cache;
	}

	/**
	 * Tries to load this file system from cache.
	 */
	public void load() {
		//if (isLoaded())
		//    throw new RuntimeException("Already loaded!");
		CacheBuffer buffer = cache.getMasterIndex().get(id);
		if (buffer == null) throw new RuntimeException("Missing filesystem file.");

		CacheBuffer unpacked = Helper.decodeFITContainer(buffer);

		unpacked.setPosition(0);

		protocol = unpacked.readUByte();
		if (protocol >= 6) version = unpacked.readInt();

		int properties = unpacked.readUByte();
		useNames = (properties & 0x1) != 0;
		useWhirlpool = (properties & 0x2) != 0;
		useCompressionLengths = (properties & 0x4) != 0;
		useFlag8 = (properties & 0x8) != 0;

		int foldersLength = protocol >= 7 ? unpacked.readSmart32() : unpacked.readUShort();
		folders = new Int2ObjectLinkedOpenHashMap<>();
		foldersByName = new Int2IntOpenHashMap();

		int[] folderIDS = new int[foldersLength];
		int[] folderNames = new int[foldersLength];
		int[] folderCRCS = new int[foldersLength];
		byte[][] folderDigests = new byte[foldersLength][64];
		int[] folderVersions = new int[foldersLength];
		int[][] folderFilesIDS = new int[foldersLength][];
		int[][] folderFilesNames = new int[foldersLength][];

		for (int offset = 0, i = 0; i < foldersLength; i++)
			folderIDS[i] = offset += (protocol >= 7 ? unpacked.readSmart32() : unpacked.readUShort());

		for (int i = 0; i < foldersLength; i++)
			folderNames[i] = useNames ? unpacked.readInt() : -1;

		for (int i = 0; i < foldersLength; i++)
			folderCRCS[i] = unpacked.readInt();

		if (useWhirlpool) {
			for (int i = 0; i < foldersLength; i++)
				unpacked.readBytes(folderDigests[i], 0, 64);
		}

		for (int i = 0; i < foldersLength; i++)
			folderVersions[i] = unpacked.readInt();

		for (int i = 0; i < foldersLength; i++) {
			int filesCount = protocol >= 7 ? unpacked.readSmart32() : unpacked.readUShort();
			folderFilesIDS[i] = new int[filesCount];
			folderFilesNames[i] = new int[filesCount];
		}

		for (int i = 0; i < foldersLength; i++)
			for (int offset = 0, a = 0; a < folderFilesIDS[i].length; a++)
				folderFilesIDS[i][a] = offset += (protocol >= 7 ? unpacked.readSmart32() : unpacked.readUShort());

		for (int i = 0; i < foldersLength; i++)
			for (int a = 0; a < folderFilesIDS[i].length; a++)
				folderFilesNames[i][a] = useNames ? unpacked.readInt() : -1;

		for (int i = 0; i < foldersLength; i++) {
			var folder = new Group(folderIDS[i], folderNames[i], folderVersions[i], folderCRCS[i], folderDigests[i],
					folderFilesIDS[i], folderFilesNames[i]);
			folders.put(folder.getID(), folder);
			if (folder.hasName()) {
				foldersByName.put(folder.getName(), folder.getID());
			}
		}

		updateHashes(buffer);

		packed = buffer;
	}

	/**
	 * Delete's all folders on this filesystem.
	 * Reset's version to 0.
	 */
	public void reset() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		changed = true;
		folders.clear();
		version = 0;
	}


	/**
	 * Finishe's any changes to this filesystem.
	 */
	public void finish() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (!needsRepack()) return;

		for (var folder : folders.values()) {
			if (folder.isFileSystemInfoChanged()) {
				if (useAutomaticVersionIncremetor) folder.setVersion(folder.getVersion() + 1);
				CacheBuffer buffer = folder.finish();
				if (!cache.getIndex(id).put(folder.getID(), buffer, buffer.getBuffer().length)) {
					throw new RuntimeException("Couldn't update folder:" + folder);
				}
			}
		}


		if (useAutomaticVersionIncremetor) version++;

		packed = pack();
		updateHashes(packed);

		if (!cache.getMasterIndex().put(id, packed, packed.getBuffer().length)) {
			throw new RuntimeException("Couldn't update packed filesystem.");
		}

		changed = false;
		for (var folder : folders.values())
			folder.markFileSystemInfoAsNotChanged();
	}

	/**
	 * Pack's this filesystem.
	 */
	public CacheBuffer pack() {
		CacheBuffer pack = new CacheBuffer(0);

		pack.writeByte(protocol);
		if (protocol >= 6) pack.writeInt(version);

		pack.writeByte((useNames ? 0x1 : 0x0) | (useWhirlpool ? 0x2 : 0x0));

		var foldersLength = foldersSize();
		if (protocol >= 7) {
			pack.writeSmart32(foldersLength);
		} else {
			pack.writeShort(foldersLength);
		}

		var foldersSorted = folders.values().parallelStream().sorted(Comparator.comparingInt(Group::getID))
				.toArray(Group[]::new);
		{
			int delta = 0;
			for (var folder : foldersSorted) {
				if (protocol >= 7) {
					pack.writeSmart32(folder.getID() - delta);
				} else {
					pack.writeShort(folder.getID() - delta);
				}
				delta = folder.getID();
			}
		}


		if (useNames) {
			for (var folder : foldersSorted)
				pack.writeInt(folder.getName());
		}

		for (var folder : foldersSorted)
			pack.writeInt(folder.getCRC32());

		if (useWhirlpool) {
			for (var folder : foldersSorted)
				pack.writeBytes(folder.getDigest(), 0, 64);
		}

		for (var folder : foldersSorted)
			pack.writeInt(folder.getVersion());

		for (var folder : foldersSorted)
			if (protocol >= 7) {
				pack.writeSmart32(folder.filesCount());
			} else {
				pack.writeShort(folder.filesCount());
			}

		for (var folder : foldersSorted) {
			int delta = 0;
			var sortedFiles = folder.getRSFiles().parallelStream().sorted(Comparator.comparingInt(RSFile::getID))
					.toArray(RSFile[]::new);
			for (var file : sortedFiles) {
				if (protocol >= 7) {
					pack.writeSmart32(file.getID() - delta);
				} else {
					pack.writeShort(file.getID() - delta);
				}
				delta = file.getID();
			}
		}

		if (useNames) {
			for (var folder : foldersSorted) {
				var sortedFiles = folder.getRSFiles().parallelStream().sorted(Comparator.comparingInt(RSFile::getID))
						.toArray(RSFile[]::new);
				for (var file : sortedFiles) {
					pack.writeInt(file.getName());
				}
			}
		}

		return Helper.encodeFITContainer(new CacheBuffer(pack.toArray(0, pack.getPosition())), version).trim();
	}

	/**
	 * Find's folder by id. Returns null if none found.
	 */
	public Group findFolderByID(int id) {
		return findFolderByID(id, null);
	}

	/**
	 * Find's folder by id. Returns null if none found.
	 */
	public Group findFolderByID(int id, int[] xtea) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		var folder = folders.get(id);
		if (folder == null) {
			return null;
		} else if (folder.isLoaded()) {
			return folder;
		}

		CacheBuffer data = cache.getIndex(this.id).get(id);
		if (data == null) throw new RuntimeException("Missing folder:" + id);
		folder.load(data, xtea);

		return folder;
	}

	/**
	 * Find's folder by name. Returns null if none found.
	 */
	public Group findFolderByName(String name) {
		return findFolderByName(name, null);
	}

	/**
	 * Find's folder by name. Returns null if none found.
	 */
	public Group findFolderByName(int name) {
		return findFolderByName(name, null);
	}

	/**
	 * Find's folder by name. Returns null if none found.
	 */
	public Group findFolderByName(String name, int[] xtea) {
		return findFolderByName(Helper.strToI(name), xtea);
	}

	/**
	 * Find's folder by name. Returns null if none found.
	 */
	public Group findFolderByName(int name, int[] xtea) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (name == -1) return null;

		var folder = folders.get(foldersByName.getOrDefault(name, -1));
		if (folder == null) {
			return null;
		} else if (folder.isLoaded()) {
			return folder;
		}

		CacheBuffer data = cache.getIndex(this.id).get(folder.getID());
		if (data == null) throw new RuntimeException("Missing folder:" + folder.getID());
		folder.load(data, xtea);

		return folder;
	}

	/**
	 * Add's new folder to this filesystem. If there's already a folder with
	 * same id then it get's overwriten.
	 */
	public void addFolder(Group group) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (!group.isLoaded()) throw new RuntimeException("folder is not loaded.");

		if (group.getID() == -1) group.setID(getFreeFolderID());
		folders.put(group.getID(), group);
		if (group.hasName()) {
			foldersByName.put(group.getName(), group.getID());
		}
		group.markFileSystemInfoAsChanged(); // cause it needs to be packed to store.
		changed = true;
	}

	/**
	 * Delete's given folder.
	 */
	public void deleteFolder(Group group) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		folders.remove(group.getID());
		foldersByName.remove(group.getID());
		changed = true;
	}

	/**
	 * Delete's all folders in this fs.
	 */
	public void deleteAllFolders() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		folders.clear();
		changed = true;
	}

	/**
	 * Load's folder if it's not yet loaded.
	 */
	public void load(Group group) {
		load(group, null);
	}

	/**
	 * Load's folder if it's not yet loaded.
	 */
	public void load(Group group, int[] xtea) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (!folders.containsKey(group.getID())) {
			return;
		} else if (group.isLoaded()) {
			return;
		} else if (group.getID() == -1) {
			return;
		}
		CacheBuffer data = cache.getIndex(id).get(group.getID());
		if (data == null) throw new RuntimeException("Missing folder:" + group.getID());
		group.load(data, xtea);
	}

	/**
	 * Finishe's any pending caches on this filesystem and then unload's some
	 * buffered files.
	 */
	public void unloadCachedFiles() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		finish();
		for (var folder : folders.values())
			if (folder.isLoaded()) folder.unload();
	}

	/**
	 * Returns highest folder id.
	 */
	public int getHighestId() {
		return getFreeFolderID() - 1;
	}

	public int size() {
		if (folders.isEmpty()) {
			return 0;
		}

		return getHighestId() + 1;
	}

	/**
	 * Get's free folder ID.
	 */
	private int getFreeFolderID() {
		if (folders.isEmpty()) return 0;
		int highest = -1;
		for (var folder : folders.values())
			if (folder.getID() > highest) highest = folder.getID();
		return highest + 1;
	}

	/**
	 * Update's crc32 and whirlpool hash to one's from given buffer.
	 */
	private void updateHashes(CacheBuffer packed) {
		crc = Helper.crc32(packed, 0, packed.getBuffer().length);
		digest = Whirlpool.whirlpool(packed.getBuffer(), 0, packed.getBuffer().length);
	}


	/**
	 * Wheter this filesystem is loaded.
	 */
	public boolean isLoaded() {
		return packed != null;
	}

	/**
	 * Wheter file system file needs repack.
	 */
	private boolean needsRepack() {
		if (changed) return true;
		for (var folder : folders.values())
			if (folder.isFileSystemInfoChanged()) return true;
		return false;
	}

	/**
	 * Get's ID of this filesystem.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Wheter this filesystem uses names.
	 */
	public boolean usesNames() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return useNames;
	}

	/**
	 * Wheter this filesystem uses whirlpool.
	 */
	public boolean usesWhirlpool() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return useWhirlpool;
	}

	/**
	 * Set's wheter this filesystem uses names.
	 */
	public void setUsesNames(boolean uses) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (this.useNames != uses) {
			changed = true;
			this.useNames = uses;
		}
	}

	/**
	 * Set's wheter this filesystem uses whirlpool.
	 */
	public void setUsesWhirlpool(boolean uses) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (this.useWhirlpool != uses) {
			changed = true;
			this.useWhirlpool = uses;
		}
	}

	/**
	 * Get's version of this filesystem.
	 */
	public int getVersion() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return version;
	}

	/**
	 * Set's version of this filesystem.
	 */
	public void setVersion(int version) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		if (this.version != version) {
			changed = true;
			this.version = version;
		}
	}

	/**
	 * Get's crc32 of packed version of this filesystem.
	 */
	public int getCRC32() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return crc;
	}

	/**
	 * Get's whirlpool digest of packed version of this filesystem.
	 */
	public byte[] getDigest() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return digest;
	}

	/**
	 * Wheter versions are automatically incremented each time finish() is
	 * called.
	 */
	public boolean usingAutomaticVersionsIncremetor() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		return useAutomaticVersionIncremetor;
	}


	/**
	 * Set's wheter to use automatic versions incrementor.
	 */
	public void setUseAutomaticVersionsIncremetor(boolean use) {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded filesystem.");
		this.useAutomaticVersionIncremetor = use;
	}

	/**
	 * Get's all folders.
	 * Returned folders array can't be modified
	 * in any way.
	 *
	 * @return
	 */
	public ObjectCollection<Group> getFolders() {
		return folders.values();
	}

	public int foldersSize() {
		return folders.size();
	}
}
