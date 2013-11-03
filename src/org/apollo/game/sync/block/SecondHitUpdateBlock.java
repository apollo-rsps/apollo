package org.apollo.game.sync.block;

/**
 * The Second Hit Update {@link SynchronizationBlock}. This is believed to be used for when multiple attacks happen at
 * once (for example, the dragon-dagger special attack). This block can be implemented by both players and npcs.
 * 
 * 
 * @author Major
 */
public class SecondHitUpdateBlock extends SynchronizationBlock {

	/**
	 * The amount of damage the hit will do.
	 */
	private final int damage;

	/**
	 * The type of hit (e.g. normal, poison).
	 */
	private final int type;

	/**
	 * The character's current health.
	 */
	private final int currentHealth;

	/**
	 * The character's maximum health.
	 */
	private final int maximumHealth;

	/**
	 * Creates a new Second Hit Update block.
	 * 
	 * @param hitDamage The damage dealt by the hit.
	 * @param hitType The type of hit.
	 * @param currentHealth The current health of the character.
	 * @param maximumHealth The maximum health of the character.
	 */
	public SecondHitUpdateBlock(int hitDamage, int hitType, int currentHealth, int maximumHealth) {
		damage = hitDamage;
		type = hitType;
		this.currentHealth = currentHealth;
		this.maximumHealth = maximumHealth;
	}

	/**
	 * Gets the current health of the character.
	 * 
	 * @return The current health;
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Gets the damage done by the hit.
	 * 
	 * @return The damage.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Gets the maximum health of the character.
	 * 
	 * @return The maximum health.
	 */
	public int getMaximumHealth() {
		return maximumHealth;
	}

	/**
	 * Gets the hit type.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

}