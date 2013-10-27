package org.apollo.game.model.def;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Item;

/**
 * Represents a type of {@link Item} which may be equipped.
 * @author Graham
 */
public final class EquipmentDefinition {

	/**
	 * The equipment definitions.
	 */
	private static final Map<Integer, EquipmentDefinition> definitions = new HashMap<Integer, EquipmentDefinition>();

	/**
	 * Initialises the equipment definitions.
	 * @param definitions The definitions.
	 */
	public static void init(EquipmentDefinition[] definitions) {
		for (int id = 0; id < definitions.length; id++) {
			EquipmentDefinition def = definitions[id];
			if (def != null) {
				if (def.getId() != id) {
					throw new RuntimeException("Item definition id mismatch!");
				}
				EquipmentDefinition.definitions.put(def.getId(), def);
			}
		}
	}

	/**
	 * Gets an equipment definition by its id.
	 * @param id The id.
	 * @return {@code null} if the item is not equipment, the definition
	 * otherwise.
	 * @throws IndexOutOfBoundsException if the id is out of bounds.
	 */
	public static EquipmentDefinition forId(int id) {
		if (id < 0 || id >= ItemDefinition.count()) {
			throw new IndexOutOfBoundsException();
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
	 * The required levels.
	 */
	private int attack = 1, strength = 1, defence = 1, ranged = 1, magic = 1;

	/**
	 * Various flags.
	 */
	private boolean twoHanded, fullBody, fullHat, fullMask;

	/**
	 * Creates a new equipment definition.
	 * @param id The id.
	 */
	public EquipmentDefinition(int id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the required levels.
	 * @param attack The required attack level.
	 * @param strength The required strength level.
	 * @param defence The required defence level.
	 * @param ranged The required ranged level.
	 * @param magic The required magic level.
	 */
	public void setLevels(int attack, int strength, int defence, int ranged, int magic) {
		this.attack = attack;
		this.strength = strength;
		this.defence = defence;
		this.ranged = ranged;
		this.magic = magic;
	}

	/**
	 * Gets the minimum attack level.
	 * @return The minimum attack level.
	 */
	public int getAttackLevel() {
		return attack;
	}

	/**
	 * Gets the minimum strength level.
	 * @return The minimum strength level.
	 */
	public int getStrengthLevel() {
		return strength;
	}

	/**
	 * Gets the minimum defence level.
	 * @return The minimum defence level.
	 */
	public int getDefenceLevel() {
		return defence;
	}

	/**
	 * Gets the minimum ranged level.
	 * @return The minimum ranged level.
	 */
	public int getRangedLevel() {
		return ranged;
	}

	/**
	 * Gets the minimum magic level.
	 * @return The minimum magic level.
	 */
	public int getMagicLevel() {
		return magic;
	}

	/**
	 * Sets the target slot.
	 * @param slot The target slot.
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Gets the target slot.
	 * @return The target slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Checks if this equipment is two-handed.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isTwoHanded() {
		return twoHanded;
	}

	/**
	 * Checks if this equipment is a full body.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullBody() {
		return fullBody;
	}

	/**
	 * Checks if this equipment is a full hat.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullHat() {
		return fullHat;
	}

	/**
	 * Checks if this equipment is a full mask.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFullMask() {
		return fullMask;
	}

	/**
	 * Sets the flags.
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

}
