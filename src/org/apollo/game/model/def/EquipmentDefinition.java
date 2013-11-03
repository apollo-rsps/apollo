package org.apollo.game.model.def;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Item;
import org.apollo.game.model.Skill;

/**
 * Represents a type of {@link Item} which may be equipped.
 * 
 * @author Graham
 */
public final class EquipmentDefinition {

	/**
	 * The equipment definitions.
	 */
	private static final Map<Integer, EquipmentDefinition> definitions = new HashMap<Integer, EquipmentDefinition>();

	/**
	 * Initialises the equipment definitions.
	 * 
	 * @param definitions The definitions.
	 */
	public static void init(EquipmentDefinition[] definitions) {
		for (int id = 0; id < definitions.length; id++) {
			EquipmentDefinition def = definitions[id];
			if (def != null) {
				if (def.getId() != id) {
					throw new RuntimeException("Equipment definition id mismatch!");
				}
				EquipmentDefinition.definitions.put(def.getId(), def);
			}
		}
	}

	public int count() {
		return definitions.size();
	}

	/**
	 * The array of skill requirement levels.
	 */
	private int[] levels = { 1, 1, 1, 1, 1, 1, 1 };

	/**
	 * Gets an equipment definition by its id.
	 * 
	 * @param id The id.
	 * @return {@code null} if the item is not equipment, the definition otherwise.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public static EquipmentDefinition forId(int id) {
		if (id < 0 || id >= ItemDefinition.count()) {
			throw new IndexOutOfBoundsException(EquipmentDefinition.class.getName() + " lookup index " + id
					+ " out of bounds.");
		}
		return definitions.get(id);
	}

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The slot this equipment goes into.
	 */
	private int slot;

	/**
	 * Various flags.
	 */
	private boolean twoHanded, fullBody, fullHat, fullMask;

	/**
	 * Creates a new equipment definition.
	 * 
	 * @param id The id.
	 */
	public EquipmentDefinition(int id) {
		this.id = id;
	}

	/**
	 * Gets the minimum attack level.
	 * 
	 * @return The minimum attack level.
	 */
	public int getAttackLevel() {
		return levels[Skill.ATTACK];
	}

	/**
	 * Gets the minimum defence level.
	 * 
	 * @return The minimum defence level.
	 */
	public int getDefenceLevel() {
		return levels[Skill.DEFENCE];
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the level for a specific skill by its id.
	 * 
	 * @param skill The skill id.
	 */
	public int getLevel(int skill) {
		if (skill < Skill.ATTACK || skill > Skill.MAGIC) {
			throw new IllegalArgumentException("Skill id out of bounds for an equipment definition.");
		}
		return levels[skill];
	}

	/**
	 * Gets the minimum magic level.
	 * 
	 * @return The minimum magic level.
	 */
	public int getMagicLevel() {
		return levels[Skill.MAGIC];
	}

	/**
	 * Gets the minimum ranged level.
	 * 
	 * @return The minimum ranged level.
	 */
	public int getRangedLevel() {
		return levels[Skill.RANGED];
	}

	/**
	 * Gets the target slot.
	 * 
	 * @return The target slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the minimum strength level.
	 * 
	 * @return The minimum strength level.
	 */
	public int getStrengthLevel() {
		return levels[Skill.STRENGTH];
	}

	/**
	 * Checks if this equipment is a full body.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullBody() {
		return fullBody;
	}

	/**
	 * Checks if this equipment is a full hat.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullHat() {
		return fullHat;
	}

	/**
	 * Checks if this equipment is a full mask.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullMask() {
		return fullMask;
	}

	/**
	 * Checks if this equipment is two-handed.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isTwoHanded() {
		return twoHanded;
	}

	/**
	 * Sets the flags.
	 * 
	 * @param twoHanded The two handed flag.
	 * @param fullBody The full body flag.
	 * @param fullHat The full hat flag.
	 * @param fullMask The full mask flag.
	 */
	public void setFlags(boolean twoHanded, boolean fullBody, boolean fullHat, boolean fullMask) {
		this.twoHanded = twoHanded;
		this.fullBody = fullBody;
		this.fullHat = fullHat;
		this.fullMask = fullMask;
	}

	/**
	 * Sets the required levels.
	 * 
	 * @param attack The required attack level.
	 * @param strength The required strength level.
	 * @param defence The required defence level.
	 * @param ranged The required ranged level.
	 * @param magic The required magic level.
	 */
	public void setLevels(int attack, int strength, int defence, int ranged, int magic) {
		levels[Skill.ATTACK] = attack;
		levels[Skill.STRENGTH] = strength;
		levels[Skill.DEFENCE] = defence;
		levels[Skill.RANGED] = ranged;
		levels[Skill.MAGIC] = magic;
	}

	/**
	 * Sets the target slot.
	 * 
	 * @param slot The target slot.
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}

}