package org.apollo.game.sync.block;

import org.apollo.game.model.Appearance;
import org.apollo.game.model.Inventory;

/**
 * The appearance {@link SynchronizationBlock}.
 * @author Graham
 */
public final class AppearanceBlock extends SynchronizationBlock {

	// TODO head icons support

	/**
	 * The player's name.
	 */
	private final long name;

	/**
	 * The player's appearance.
	 */
	private final Appearance appearance;

	/**
	 * The player's combat level.
	 */
	private final int combat;

	/**
	 * The player's total skill level (or 0).
	 */
	private final int skill;

	/**
	 * The player's equipment.
	 */
	private final Inventory equipment;

	/**
	 * Creates the appearance block.
	 * @param name The player's name.
	 * @param appearance The appearance.
	 * @param combat The player's combat.
	 * @param skill The player's skill, or 0 if showing the combat level.
	 * @param equipment The player's equipment.
	 */
	AppearanceBlock(long name, Appearance appearance, int combat, int skill, Inventory equipment) {
		this.name = name;
		this.appearance = appearance;
		this.combat = combat;
		this.skill = skill;
		this.equipment = equipment.clone();
	}

	/**
	 * Gets the player's name.
	 * @return The player's name.
	 */
	public long getName() {
		return name;
	}

	/**
	 * Gets the player's appearance.
	 * @return The player's appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the player's combat level.
	 * @return The player's combat level.
	 */
	public int getCombatLevel() {
		return combat;
	}

	/**
	 * Gets the player's skill level.
	 * @return The player's skill level.
	 */
	public int getSkillLevel() {
		return skill;
	}

	/**
	 * Gets the player's equipment.
	 * @return The player's equipment.
	 */
	public Inventory getEquipment() {
		return equipment;
	}

}
