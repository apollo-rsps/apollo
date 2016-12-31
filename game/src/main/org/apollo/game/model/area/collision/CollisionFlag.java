package org.apollo.game.model.area.collision;

import org.apollo.game.model.entity.EntityType;

/**
 * A type of flag in a {@link CollisionMatrix}.
 *
 * @author Major
 */
public enum CollisionFlag {

	/**
	 * The walk north west flag.
	 */
	MOB_NORTH_WEST(1),

	/**
	 * The walk north flag.
	 */
	MOB_NORTH(2),

	/**
	 * The walk north east flag.
	 */
	MOB_NORTH_EAST(3),

	/**
	 * The walk east flag.
	 */
	MOB_EAST(4),

	/**
	 * The walk south east flag.
	 */
	MOB_SOUTH_EAST(5),

	/**
	 * The walk south flag.
	 */
	MOB_SOUTH(6),

	/**
	 * The walk south west flag.
	 */
	MOB_SOUTH_WEST(7),

	/**
	 * The walk west flag.
	 */
	MOB_WEST(8),

	/**
	 * The projectile north west flag.
	 */
	PROJECTILE_NORTH_WEST(9),

	/**
	 * The projectile north flag.
	 */
	PROJECTILE_NORTH(10),

	/**
	 * The projectile north east flag.
	 */
	PROJECTILE_NORTH_EAST(11),

	/**
	 * The projectile east flag.
	 */
	PROJECTILE_EAST(12),

	/**
	 * The projectile south east flag.
	 */
	PROJECTILE_SOUTH_EAST(13),

	/**
	 * The projectile south flag.
	 */
	PROJECTILE_SOUTH(14),

	/**
	 * The projectile south west flag.
	 */
	PROJECTILE_SOUTH_WEST(15),

	/**
	 * The projectile west flag.
	 */
	PROJECTILE_WEST(16);

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
		return new CollisionFlag[] {
			MOB_NORTH_WEST,
			MOB_NORTH,
			MOB_NORTH_EAST,
			MOB_WEST,
			MOB_EAST,
			MOB_SOUTH_WEST,
			MOB_SOUTH,
			MOB_SOUTH_EAST
		};
	}

	/**
	 * Returns an array of CollisionFlags that indicate if a Projectile can be positioned on a tile.
	 *
	 * @return The array of CollisionFlags.
	 */
	public static CollisionFlag[] projectiles() {
		return new CollisionFlag[] {
			PROJECTILE_NORTH_WEST,
			PROJECTILE_NORTH,
			PROJECTILE_NORTH_EAST,
			PROJECTILE_WEST,
			PROJECTILE_EAST,
			PROJECTILE_SOUTH_WEST,
			PROJECTILE_SOUTH,
			PROJECTILE_SOUTH_EAST
		};
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
	 * Gets this CollisionFlag, as a {@code short}.
	 *
	 * @return The value, as a {@code short}.
	 */
	public short asShort() {
		return (short) (1 << bit);
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