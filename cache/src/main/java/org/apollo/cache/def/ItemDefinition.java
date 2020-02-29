package org.apollo.cache.def;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

/**
 * Represents a type of Item.
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
	 * Gets the total number of item definitions.
	 *
	 * @return The count.
	 */
	public static int count() {
		return definitions.length;
	}

	/**
	 * Gets the array of item definitions.
	 *
	 * @return The definitions.
	 */
	public static ItemDefinition[] getDefinitions() {
		return definitions;
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
			if (def == null) {
				ItemDefinition.definitions[id] = def = new ItemDefinition(id);
			}

			if (def.getId() != id) {
				throw new RuntimeException("Item definition id mismatch.");
			}
			if (def.isNote()) {
				def.generate(ItemGenerationType.NOTE, ItemDefinition.lookup(def.getNoteInfoId()),
						ItemDefinition.lookup(def.getNoteGraphicId()));
				notes.put(def.getNoteInfoId(), def.getId());
			} else if (def.isLoaner()) {
				def.generate(ItemGenerationType.LOAN, ItemDefinition.lookup(def.getLoanInfo()),
						ItemDefinition.lookup(def.getLoanGraphicId()));
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
		Preconditions.checkElementIndex(id, definitions.length, "Id out of bounds.");
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
	private final String[] groundActions = new String[5];

	/**
	 * The item's id.
	 */
	private final int id;

	/**
	 * The inventory actions array.
	 */
	private final String[] inventoryActions = new String[5];

	/**
	 * A flag indicating if this item is members only.
	 */
	private boolean members = false;

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
	private boolean stackable = false;

	/**
	 * This item's team.
	 */
	private int team;

	/**
	 * The item's floor value.
	 */
	private int value = 1;

	private int baseModel;
	private int modelScale;
	private int modelPitch;
	private int modelRoll;
	private int modelTranslateX;
	private int modelTranslateY;
	private int stackType;
	private int primaryMaleModel;
	private int maleYOffset;
	private int secondaryMaleModel;
	private int primaryFemaleModel;
	private int femaleYOffset;
	private int secondaryFemaleModel;
	private short[] originalModelColors;
	private short[] modifiedModelColors;
	private short[] modifiedTextureColors;
	private short[] originalTextureColors;
	private boolean nonExchangeItem;
	private int tertiaryMaleModel;
	private int tertiaryFemaleModel;
	private int maleHead;
	private int femaleHead;
	private int secondaryMaleHead;
	private int secondaryFemaleHead;
	private int modelYaw;
	private int[] stackIds;
	private int[] stackAmounts;
	private int scaleX;
	private int scaleY;
	private int scaleZ;
	private int shiftClickDropIndex;
	private byte ambience;
	private byte contrast;
	private int loanInfo = -1;
	private int loanGraphicId = -1;
	private int placeHolderInfo;
	private int placeHolderTemplate;
	private Int2ObjectArrayMap<Object> parameters;

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
		Preconditions.checkElementIndex(id, groundActions.length, "Ground action id is out of bounds.");
		return groundActions[id];
	}

	/**
	 * Gets this item's id.
	 *
	 * @return The id.
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
		Preconditions.checkElementIndex(id, inventoryActions.length, "Inventory action id is out of bounds.");
		return inventoryActions[id];
	}

	public int getBaseModel() {
		return baseModel;
	}

	public int getModelScale() {
		return modelScale;
	}

	public int getModelPitch() {
		return modelPitch;
	}

	public int getModelRoll() {
		return modelRoll;
	}

	public int getModelTranslateX() {
		return modelTranslateX;
	}

	public int getModelTranslateY() {
		return modelTranslateY;
	}

	public int getStackType() {
		return stackType;
	}

	public int getPrimaryMaleModel() {
		return primaryMaleModel;
	}

	public int getMaleYOffset() {
		return maleYOffset;
	}

	public int getSecondaryMaleModel() {
		return secondaryMaleModel;
	}

	public int getPrimaryFemaleModel() {
		return primaryFemaleModel;
	}

	public int getFemaleYOffset() {
		return femaleYOffset;
	}

	public int getSecondaryFemaleModel() {
		return secondaryFemaleModel;
	}

	public short[] getOriginalModelColors() {
		return originalModelColors;
	}

	public short[] getModifiedModelColors() {
		return modifiedModelColors;
	}

	public short[] getModifiedTextureColors() {
		return modifiedTextureColors;
	}

	public short[] getOriginalTextureColors() {
		return originalTextureColors;
	}

	public boolean isNonExchangeItem() {
		return nonExchangeItem;
	}

	public int getTertiaryMaleModel() {
		return tertiaryMaleModel;
	}

	public int getTertiaryFemaleModel() {
		return tertiaryFemaleModel;
	}

	public int getMaleHead() {
		return maleHead;
	}

	public int getFemaleHead() {
		return femaleHead;
	}

	public int getSecondaryMaleHead() {
		return secondaryMaleHead;
	}

	public int getSecondaryFemaleHead() {
		return secondaryFemaleHead;
	}

	public int getModelYaw() {
		return modelYaw;
	}

	public int[] getStackIds() {
		return stackIds;
	}

	public int[] getStackAmounts() {
		return stackAmounts;
	}

	public int getScaleX() {
		return scaleX;
	}

	public int getScaleY() {
		return scaleY;
	}

	public int getScaleZ() {
		return scaleZ;
	}

	public int getShiftClickDropIndex() {
		return shiftClickDropIndex;
	}

	public byte getAmbience() {
		return ambience;
	}

	public byte getContrast() {
		return contrast;
	}

	public int getLoanInfo() {
		return loanInfo;
	}

	public int getLoanGraphicId() {
		return loanGraphicId;
	}

	public int getPlaceHolderInfo() {
		return placeHolderInfo;
	}

	public int getPlaceHolderTemplate() {
		return placeHolderTemplate;
	}

	public Int2ObjectArrayMap<Object> getParameters() {
		return parameters;
	}

	/**
	 * Gets this item's name.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets this item's note graphic id.
	 *
	 * @return The note graphic id.
	 */
	public int getNoteGraphicId() {
		return noteGraphicId;
	}

	/**
	 * Gets this item's note info id.
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
	 * Gets this item's value.
	 *
	 * @return The value.
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
	 * Checks if this item is loaned.
	 *
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isLoaner() {
		return loanInfo != -1 && loanGraphicId != -1;
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
	 * Sets this item's description.
	 *
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets a ground action.
	 *
	 * @param id     The id.
	 * @param action The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public void setGroundAction(int id, String action) {
		Preconditions.checkElementIndex(id, groundActions.length, "Ground action id is out of bounds.");
		groundActions[id] = action;
	}

	/**
	 * Sets an inventory action.
	 *
	 * @param id     The id.
	 * @param action The action.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public void setInventoryAction(int id, String action) {
		Preconditions.checkElementIndex(id, inventoryActions.length, "Inventory action id is out of bounds.");
		inventoryActions[id] = action;
	}

	/**
	 * Sets this item's members only flag.
	 *
	 * @param members The flag.
	 */
	public void setMembersOnly(boolean members) {
		this.members = members;
	}

	/**
	 * Sets this item's name.
	 *
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets this item's note graphic id.
	 *
	 * @param noteGraphicId The note graphic id.
	 */
	public void setNoteGraphicId(int noteGraphicId) {
		this.noteGraphicId = noteGraphicId;
	}

	/**
	 * Sets this item's note info id.
	 *
	 * @param noteInfoId The note info id.
	 */
	public void setNoteInfoId(int noteInfoId) {
		this.noteInfoId = noteInfoId;
	}

	/**
	 * Sets this item's stackable flag.
	 *
	 * @param stackable The stackable flag.
	 */
	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	/**
	 * Sets this item's team.
	 *
	 * @param team The team.
	 */
	public void setTeam(int team) {
		this.team = team;
	}

	/**
	 * sets this item's value.
	 *
	 * @param value The value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	public void setBaseModel(int baseModel) {
		this.baseModel = baseModel;
	}

	public void setModelScale(int modelScale) {
		this.modelScale = modelScale;
	}

	public void setModelPitch(int modelPitch) {
		this.modelPitch = modelPitch;
	}

	public void setModelRoll(int modelRoll) {
		this.modelRoll = modelRoll;
	}

	public void setModelTranslateX(int modelTranslateX) {
		this.modelTranslateX = modelTranslateX;
	}

	public void setModelTranslateY(int modelTranslateY) {
		this.modelTranslateY = modelTranslateY;
	}

	public void setStackType(int stackType) {
		this.stackType = stackType;
	}

	public void setPrimaryMaleModel(int primaryMaleModel) {
		this.primaryMaleModel = primaryMaleModel;
	}

	public void setMaleYOffset(int maleYOffset) {
		this.maleYOffset = maleYOffset;
	}

	public void setSecondaryMaleModel(int secondaryMaleModel) {
		this.secondaryMaleModel = secondaryMaleModel;
	}

	public void setPrimaryFemaleModel(int primaryFemaleModel) {
		this.primaryFemaleModel = primaryFemaleModel;
	}

	public void setFemaleYOffset(int femaleYOffset) {
		this.femaleYOffset = femaleYOffset;
	}

	public void setSecondaryFemaleModel(int secondaryFemaleModel) {
		this.secondaryFemaleModel = secondaryFemaleModel;
	}

	public void setOriginalModelColors(short[] originalModelColors) {
		this.originalModelColors = originalModelColors;
	}

	public void setModifiedModelColors(short[] modifiedModelColors) {
		this.modifiedModelColors = modifiedModelColors;
	}

	public void setModifiedTextureColors(short[] modifiedTextureColors) {
		this.modifiedTextureColors = modifiedTextureColors;
	}

	public void setOriginalTextureColors(short[] originalTextureColors) {
		this.originalTextureColors = originalTextureColors;
	}

	public void setNonExchangeItem(boolean nonExchangeItem) {
		this.nonExchangeItem = nonExchangeItem;
	}

	public void setTertiaryMaleModel(int tertiaryMaleModel) {
		this.tertiaryMaleModel = tertiaryMaleModel;
	}

	public void setTertiaryFemaleModel(int tertiaryFemaleModel) {
		this.tertiaryFemaleModel = tertiaryFemaleModel;
	}

	public void setMaleHead(int maleHead) {
		this.maleHead = maleHead;
	}

	public void setFemaleHead(int femaleHead) {
		this.femaleHead = femaleHead;
	}

	public void setSecondaryMaleHead(int secondaryMaleHead) {
		this.secondaryMaleHead = secondaryMaleHead;
	}

	public void setSecondaryFemaleHead(int secondaryFemaleHead) {
		this.secondaryFemaleHead = secondaryFemaleHead;
	}

	public void setModelYaw(int modelYaw) {
		this.modelYaw = modelYaw;
	}

	public void setStackIds(int[] stackIds) {
		this.stackIds = stackIds;
	}

	public void setStackAmounts(int[] stackAmounts) {
		this.stackAmounts = stackAmounts;
	}

	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}

	public void setScaleZ(int scaleZ) {
		this.scaleZ = scaleZ;
	}

	public void setShiftClickDropIndex(int shiftClickDropIndex) {
		this.shiftClickDropIndex = shiftClickDropIndex;
	}

	public void setAmbience(byte ambience) {
		this.ambience = ambience;
	}

	public void setContrast(byte contrast) {
		this.contrast = contrast;
	}

	public void setLoanInfo(int loanInfo) {
		this.loanInfo = loanInfo;
	}

	public void setLoanGraphicId(int loanGraphicId) {
		this.loanGraphicId = loanGraphicId;
	}

	public void setPlaceHolderInfo(int placeHolderInfo) {
		this.placeHolderInfo = placeHolderInfo;
	}

	public void setPlaceHolderTemplate(int placeHolderTemplate) {
		this.placeHolderTemplate = placeHolderTemplate;
	}

	public void setParameters(Int2ObjectArrayMap<Object> parameters) {
		this.parameters = parameters;
	}

	public enum ItemGenerationType {
		NOTE, LOAN, BOUGHT, COMBINE
	}

	void generate(ItemGenerationType type, ItemDefinition template, ItemDefinition info) {
		baseModel = template.baseModel;
		modelScale = template.modelScale;
		modelPitch = template.modelPitch;
		modelRoll = template.modelRoll;
		modelYaw = template.modelYaw;
		modelTranslateX = template.modelTranslateX;
		modelTranslateY = template.modelTranslateY;

		ItemDefinition replacements = (type == ItemGenerationType.NOTE) ? template : info;
		originalModelColors = replacements.originalModelColors;
		modifiedModelColors = replacements.modifiedModelColors;
		originalTextureColors = replacements.originalTextureColors;
		modifiedTextureColors = replacements.modifiedTextureColors;

		name = info.name;
		members = info.members;
		if (type == ItemGenerationType.NOTE) {
			String prefix = "a";
			char firstChar = name == null ? 'n' : name.charAt(0);

			if (firstChar == 'A' || firstChar == 'E' || firstChar == 'I' || firstChar == 'O' || firstChar == 'U') {
				prefix = "an";
			}

			value = info.value;
			stackType = 1;
			description = "Swap this note at any bank for " + prefix + " " + name + ".";
		} else {
			value = 0;
			stackType = info.stackType;
			primaryMaleModel = info.primaryMaleModel;
			secondaryMaleModel = info.secondaryMaleModel;
			tertiaryMaleModel = info.tertiaryMaleModel;
			primaryFemaleModel = info.primaryFemaleModel;
			secondaryFemaleModel = info.secondaryFemaleModel;
			tertiaryFemaleModel = info.tertiaryFemaleModel;
			maleHead = info.maleHead;
			secondaryMaleHead = info.secondaryMaleHead;
			femaleHead = info.femaleHead;
			secondaryFemaleHead = info.secondaryFemaleHead;
			team = info.team;
			System.arraycopy(info.groundActions, 0, groundActions, 0, groundActions.length);
			System.arraycopy(info.inventoryActions, 0, inventoryActions, 0, 4);
			parameters = info.parameters;
			inventoryActions[4] = "Discard";
			value = 0;
		}
	}
}
