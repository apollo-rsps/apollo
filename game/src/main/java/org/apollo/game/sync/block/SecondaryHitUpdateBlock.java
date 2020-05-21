package org.apollo.game.sync.block;

/**
 * The secondary hit update {@link SynchronizationBlock}. This is used for when multiple attacks happen at once (for
 * example, the dragon-dagger special attack). Both players and npcs can utilise this block.
 *
 * @author Major
 */
public final class SecondaryHitUpdateBlock extends SynchronizationBlock {

	/**
	 * The mob's current health.
	 */
	private final int currentHealth;

	/**
	 * The amount of damage the hit will do.
	 */
	private final int damage;

	/**
	 * The mob's maximum health.
	 */
	private final int maximumHealth;

	/**
	 * The type of hit (e.g. normal, poison).
	 */
	private final int type;

	/**
	 * Creates a new secondary hit update block.
	 *
	 * @param damage The damage dealt by the hit.
	 * @param type The type of hit.
	 * @param currentHealth The current health of the mob.
	 * @param maximumHealth The maximum health of the mob.
	 */
	SecondaryHitUpdateBlock(int damage, int type, int currentHealth, int maximumHealth) {
		this.damage = damage;
		this.type = type;
		this.currentHealth = currentHealth;
		this.maximumHealth = maximumHealth;
	}

	/**
	 * Gets the current health of the mob.
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
	 * Gets the maximum health of the mob.
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