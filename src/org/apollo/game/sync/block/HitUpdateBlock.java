package org.apollo.game.sync.block;

/**
 * The Hit Update {@link SynchronizationBlock}. This is a simple implementation designed so that you can integrate it
 * easily with your combat system. Both npcs and players can implement this block.
 * 
 * @author Major
 */
public class HitUpdateBlock extends SynchronizationBlock {

	/**
	 * The amount of damage the hit will do.
	 */
	private final int damage;

	/**
	 * The type of hit (e.g. normal, poison).
	 */
	private final int type;

	/**
	 * The {@link org.apollo.game.model.Character}'s current health.
	 */
	private final int currentHealth;

	/**
	 * The {@link org.apollo.game.model.Character}'s maximum health.
	 */
	private final int maximumHealth;

	/**
	 * Creates a new Hit Update block.
	 * 
	 * @param hitDamage The damage dealt by the hit.
	 * @param hitType The type of hit.
	 * @param currentHealth The current health of the {@link org.apollo.game.model.Character}.
	 * @param maximumHealth The maximum health of the {@link org.apollo.game.model.Character}.
	 */
	public HitUpdateBlock(int hitDamage, int hitType, int currentHealth, int maximumHealth) {
		damage = hitDamage;
		type = hitType;
		this.currentHealth = currentHealth;
		this.maximumHealth = maximumHealth;
	}

	/**
	 * Gets the current health of the {@link org.apollo.game.model.Character}.
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
	 * Gets the maximum health of the {@link org.apollo.game.model.Character}.
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