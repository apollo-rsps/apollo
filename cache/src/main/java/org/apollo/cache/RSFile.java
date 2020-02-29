package org.apollo.cache;

public class RSFile {

	/**
	 * Contains file ID.
	 */
	private int id;
	/**
	 * Contains name hash of this file.
	 */
	private int name;
	/**
	 * Contains file data if loaded.
	 */
	private CacheBuffer data;
	/**
	 * Wheter filesystem info about this file
	 * changed in any way.
	 */
	private boolean fsInfoChanged;
	/**
	 * Wheter file data was changed.
	 */
	private boolean dataChanged;

	@SuppressWarnings("unused")
	private RSFile() {

	}

	/**
	 * This constructor can be used only by FS
	 * loader.
	 */
	public RSFile(int fileID, int name) {
		this(fileID,name,null);
	}


	/**
	 * Construct's new file with autoassigned ID.
	 */
	public RSFile(String name, CacheBuffer data) {
		this(-1, Helper.strToI(name),data);
	}

	/**
	 * Construct's new file with autoassigned ID.
	 */
	public RSFile(CacheBuffer data) {
		this(-1,-1,data);
	}

	public RSFile(int filID, CacheBuffer data) {
		this(filID,-1,data);
	}

	public RSFile(int fileID, String name, CacheBuffer data) {
		this(fileID,Helper.strToI(name),data);
	}

	public RSFile(int fileID, int name, CacheBuffer data) {
		this.id = fileID;
		this.name = name;
		this.data = data;
	}

	/**
	 * Copies this file, including the
	 * buffer if it's present.
	 */
	public RSFile copy() {
		RSFile f = new RSFile(id,name);
		if (data != null) {
			f.data = new CacheBuffer(data.toArray(0, data.getBuffer().length),data.getPosition());
		}
		return f;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file: " + id);
		if (this.id != id) {
			this.id = id;
			fsInfoChanged = true;
		}
	}

	public int getName() {
		return name;
	}

	public void setName(String name) {
		setName(Helper.strToI(name));
	}

	public void setName(int name) {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");

		if (this.name != name) {
			fsInfoChanged = true;
			this.name = name;
		}
	}

	public CacheBuffer getData() {
		return data;
	}

	/**
	 * Load's this file.
	 */
	public void load(CacheBuffer data) {
		if (isLoaded())
			throw new RuntimeException("Already loaded.");
		this.data = data;
	}

	/**
	 * Set's file data.
	 */
	public void setData(CacheBuffer data) {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		if (this.data != data) {
			dataChanged = fsInfoChanged = true;
			this.data = data;
		}
	}

	/**
	 * Wheter this file is loaded.
	 */
	public boolean isLoaded() {
		return data != null;
	}

	/**
	 * Unload's this file , can be called
	 * only by folder unload() method.
	 */
	public void unload() {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		data = null;
	}

	/**
	 * Whether filesystem info about this file changed.
	 */
	public boolean isFileSystemInfoChanged() {
		return fsInfoChanged;
	}

	/**
	 * Wheter data was changed.
	 */
	public boolean isDataChanged() {
		return dataChanged;
	}

	/**
	 * Mark's this file as not changed.
	 */
	public void markFileSystemInfoAsNotChanged() {
		fsInfoChanged = false;
	}

	/**
	 * Mark's data as not changed.
	 */
	public void markDataAsNotChanged() {
		dataChanged = false;
	}

	public boolean hasName() {
		return name != -1;
	}
}
