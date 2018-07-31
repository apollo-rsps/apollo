package org.apollo.game.model.entity.obj;

import java.util.Arrays;

/**
 * The type of an object, which affects specified behaviour (such as whether it displaces existing objects). TODO
 * complete this...
 *
 * @author Major
 * @author Scu11
 */
public enum ObjectType {

	/**
	 * A wall that is presented lengthwise with respect to the tile.
	 */
	LENGTHWISE_WALL(0, ObjectGroup.WALL),

	/**
	 * A triangular object positioned in the corner of the tile.
	 */
	TRIANGULAR_CORNER(1, ObjectGroup.WALL),

	/**
	 * A corner for a wall, where the model is placed on two perpendicular edges of a single tile.
	 */
	WALL_CORNER(2, ObjectGroup.WALL),

	/**
	 * A rectangular object positioned in the corner of the tile.
	 */
	RECTANGULAR_CORNER(3, ObjectGroup.WALL),

	/**
	 * A wall joint that is presented diagonally with respect to the tile.
	 */
	DIAGONAL_WALL(9, ObjectGroup.INTERACTABLE_OBJECT),

	/**
	 * An object that can be interacted with by a player.
	 */
	INTERACTABLE(10, ObjectGroup.INTERACTABLE_OBJECT),

	/**
	 * An {@link #INTERACTABLE} object, rotated {@code pi / 2} radians.
	 */
	DIAGONAL_INTERACTABLE(11, ObjectGroup.INTERACTABLE_OBJECT),

	/**
	 * A decoration positioned on the floor.
	 */
	FLOOR_DECORATION(22, ObjectGroup.GROUND_DECORATION);

	/**
	 * The ObjectGroup this type belongs in.
	 */
	private final ObjectGroup group;

	/**
	 * The integer value of this ObjectType.
	 */
	private final int value;

	/**
	 * Creates the ObjectType.
	 *
	 * @param value The integer value of this ObjectType.
	 * @param group The ObjectGroup of this type.
	 */
	ObjectType(int value, ObjectGroup group) {
		this.value = value;
		this.group = group;
	}

	/**
	 * Gets the type with the specified value.
	 *
	 * @param value The value.
	 * @return The type.
	 */
	public static ObjectType valueOf(int value) {
		return Arrays.stream(values())
			.filter(type -> type.value == value)
			.findAny().orElse(null);
	}

	/**
	 * Gets the {@link ObjectGroup} of this ObjectType.
	 *
	 * @return The group.
	 */
	public ObjectGroup getGroup() {
		return group;
	}

	/**
	 * Gets the integer value of this ObjectType.
	 *
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

}