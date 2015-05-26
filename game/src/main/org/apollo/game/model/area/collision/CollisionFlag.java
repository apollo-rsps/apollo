package org.apollo.game.model.area.collision;

import org.apollo.game.model.entity.EntityType;

/**
 * A type of flag in a {@link CollisionMatrix}.
 *
 * @author Major
 */
public enum CollisionFlag {

	/**
	 * The walk north flag.
	 */
	MOB_NORTH(0),

	/**
	 * The walk east flag.
	 */
	MOB_EAST(1),

	/**
	 * The walk south flag.
	 */
	MOB_SOUTH(2),

	/**
	 * The walk west flag.
	 */
	MOB_WEST(3),

	/**
	 * The projectile north flag.
	 */
	PROJECTILE_NORTH(4),

	/**
	 * The projectile east flag.
	 */
	PROJECTILE_EAST(5),

	/**
	 * The projectile south flag.
	 */
	PROJECTILE_SOUTH(6),

	/**
	 * The projectile west flag.
	 */
	PROJECTILE_WEST(7);

	/**
	 * Returns an array of CollisionFlags that indicate if the specified {@link EntityType} can be positioned on a tile.
	 *
	 * @param type The EntityType.
	 * @return The array of CollisionFlags.
	 */
	public static CollisionFlag[] forType(EntityType type) {
		return type == EntityType.PROJECTILE ? projectiles() : mobs();
	}

	/**
	 * Returns an array of CollisionFlags that indicate if a Mob can be positioned on a tile.
	 *
	 * @return The array of CollisionFlags.
	 */
	public static CollisionFlag[] mobs() {
		return new CollisionFlag[] { MOB_NORTH, MOB_EAST, MOB_SOUTH, MOB_WEST };
	}

	/**
	 * Returns an array of CollisionFlags that indicate if a Projectile can be positioned on a tile.
	 *
	 * @return The array of CollisionFlags.
	 */
	public static CollisionFlag[] projectiles() {
		return new CollisionFlag[] { PROJECTILE_NORTH, PROJECTILE_EAST, PROJECTILE_SOUTH, PROJECTILE_WEST };
	}

	/**
	 * The index of the bit this flag is stored in.
	 */
	private final int bit;

	/**
	 * Creates the CollisionFlag.
	 *
	 * @param bit The index of the bit this flag is stored in.
	 */
	private CollisionFlag(int bit) {
		this.bit = bit;
	}

	/**
	 * Gets this CollisionFlag, as a {@code byte}.
	 *
	 * @return The value, as a {@code byte}.
	 */
	public byte asByte() {
		return (byte) (1 << bit);
	}

	/**
	 * Gets the index of the bit this flag is stored in.
	 *
	 * @return The index of the bit.
	 */
	public int getBit() {
		return bit;
	}

}