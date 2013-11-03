package org.apollo.game.model.def;

import org.apollo.game.model.Item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Represents a type of {@link Item}.
 * 
 * @author Graham
 */
public final class ItemDefinition {

	/**
	 * The item definitions.
	 */
	private static ItemDefinition[] definitions;

	/**
	 * A map of item ids to noted ids.
	 */
	private static final BiMap<Integer, Integer> notes = HashBiMap.create();

	/**
	 * A map of noted ids to item ids.
	 */
	private static final BiMap<Integer, Integer> notesInverse = notes.inverse();

	/**
	 * Gets the total number of items.
	 * 
	 * @return The total number of items.
	 */
	public static int count() {
		return definitions.length;
	}

	/**
	 * Initialises the class with the specified set of definitions.
	 * 
	 * @param definitions The definitions.
	 * @throws RuntimeException If there is an id mismatch.
	 */
	public static void init(ItemDefinition[] definitions) {
		ItemDefinition.definitions = definitions;
		for (int id = 0; id < definitions.length; id++) {
			ItemDefinition def = definitions[id];
			if (def.getId() != id) {
				throw new RuntimeException("Item definition id mismatch!");
			}
			if (def.isNote()) {
				def.toNote();
				notes.put(def.getNoteInfoId(), def.getId());
			}
		}
	}

	/**
	 * Converts an item id to a noted id.
	 * 
	 * @param id The item id.
	 * @return The noted id.
	 */
	public static int itemToNote(int id) {
		Integer entry = notes.get(id);
		if (entry == null) {
			return id;
		}
		return entry;
	}

	/**
	 * Gets the item definition for the specified id.
	 * 
	 * @param id The id.
	 * @return The definition.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public static ItemDefinition lookup(int id) {
		if (id < 0 || id >= definitions.length) {
			throw new IndexOutOfBoundsException();
		}
		return definitions[id];
	}

	/**
	 * Converts a noted id to the normal item id.
	 * 
	 * @param id The note id.
	 * @return The item id.
	 */
	public static int noteToItem(int id) {
		Integer entry = notesInverse.get(id);
		if (entry == null) {
			return id;
		}
		return entry;
	}

	/**
	 * The description of the item.
	 */
	private String description;

	/**
	 * The ground actions array.
	 */
	private String[] groundActions = new String[5];

	/**
	 * The item's id.
	 */
	private final int id;

	/**
	 * The inventory actions array.
	 */
	private String[] inventoryActions = new String[5];

	/**
	 * A flag indicating if this item is members only.
	 */
	private boolean members;

	/**
	 * The name of the item.
	 */
	private String name;

	/**
	 * The id of the item to copy note graphics from.
	 */
	private int noteGraphicId = -1;

	/**
	 * The id of the item to copy note info from.
	 */
	private int noteInfoId = -1;

	/**
	 * A flag indicating if this item is stackable.
	 */
	private boolean stackable;

	/**
	 * This item's team.
	 */
	private int team;

	/**
	 * The value of the item.
	 */
	private int value;

	/**
	 * Creates an item definition with the default values.
	 * 
	 * @param id The item's id.
	 */
	public ItemDefinition(int id) {
		this.id = id;
	}

	/**
	 * Gets the description of this item.
	 * 
	 * @return The item's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets a ground action.
	 * 
	 * @param id The id.
	 * @return The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public String getGroundAction(int id) {
		if (id < 0 || id >= groundActions.length) {
			throw new IndexOutOfBoundsException();
		}
		return groundActions[id];
	}

	/**
	 * Gets the item's id.
	 * 
	 * @return The item's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets an inventory action.
	 * 
	 * @param id The id.
	 * @return The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public String getInventoryAction(int id) {
		if (id < 0 || id >= inventoryActions.length) {
			throw new IndexOutOfBoundsException();
		}
		return inventoryActions[id];
	}

	/**
	 * Gets the name of this item.
	 * 
	 * @return The name of this item, or {@code null} if it has no name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the note graphic id.
	 * 
	 * @return The note graphic id.
	 */
	public int getNoteGraphicId() {
		return noteGraphicId;
	}

	/**
	 * Gets the note info id.
	 * 
	 * @return The note info id.
	 */
	public int getNoteInfoId() {
		return noteInfoId;
	}

	/**
	 * Gets this item's team.
	 * 
	 * @return The team.
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * Gets the value of this item.
	 * 
	 * @return The value of this item.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Checks if this item is members only.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMembersOnly() {
		return members;
	}

	/**
	 * Checks if this item is a note.
	 * 
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isNote() {
		return noteGraphicId != -1 && noteInfoId != -1;
	}

	/**
	 * Checks if the item specified by this definition is stackable.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isStackable() {
		return stackable;
	}

	/**
	 * Sets the description of this item.
	 * 
	 * @param description The item's description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets a ground action.
	 * 
	 * @param id The id.
	 * @param action The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public void setGroundAction(int id, String action) {
		if (id < 0 || id >= groundActions.length) {
			throw new IndexOutOfBoundsException();
		}
		groundActions[id] = action;
	}

	/**
	 * Sets an inventory action.
	 * 
	 * @param id The id.
	 * @param action The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public void setInventoryAction(int id, String action) {
		if (id < 0 || id >= inventoryActions.length) {
			throw new IndexOutOfBoundsException();
		}
		inventoryActions[id] = action;
	}

	/**
	 * Sets the members only flag.
	 * 
	 * @param members The members only flag.
	 */
	public void setMembersOnly(boolean members) {
		this.members = members;
	}

	/**
	 * Sets the name of this item.
	 * 
	 * @param name The item's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the note graphic id.
	 * 
	 * @param noteGraphicId The note graphic id.
	 */
	public void setNoteGraphicId(int noteGraphicId) {
		this.noteGraphicId = noteGraphicId;
	}

	/**
	 * Sets the note info id.
	 * 
	 * @param noteInfoId The note info id.
	 */
	public void setNoteInfoId(int noteInfoId) {
		this.noteInfoId = noteInfoId;
	}

	/**
	 * Sets the stackable flag.
	 * 
	 * @param stackable The stackable flag.
	 */
	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	/**
	 * Sets this items team.
	 * 
	 * @param team The team.
	 */
	public void setTeam(int team) {
		this.team = team;
	}

	/**
	 * Sets the value of this item.
	 * 
	 * @param value The value of this item.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Converts this item to a note, if possible.
	 * 
	 * @throws IllegalStateException If {@link ItemDefinition#isNote()} returns {@code false}.
	 */
	public void toNote() {
		if (isNote()) {
			if (description != null && description.startsWith("Swap this note at any bank for ")) {
				return; // already converted TODO better way of checking?
			}

			ItemDefinition infoDef = lookup(noteInfoId);
			name = infoDef.name;
			members = infoDef.members;
			value = infoDef.value;

			String prefix = "a";
			char firstChar = name == null ? 'n' : name.charAt(0);

			if (firstChar == 'A' || firstChar == 'E' || firstChar == 'I' || firstChar == 'O' || firstChar == 'U') {
				prefix = "an";
			}

			description = "Swap this note at any bank for " + prefix + " " + name + ".";
			stackable = true;
		} else {
			throw new IllegalStateException();
		}
	}

}