package org.apollo.game.model.entity.obj;

import java.util.Arrays;

/**
 * The group of an object, which indicates its general class (e.g. if it's a wall, or a floor decoration).
 *
 * @author Major
 * @author Scu11
 */
public enum ObjectGroup {

	/**
	 * The wall object group, which may block a tile.
	 */
	WALL(0),

	/**
	 * The wall decoration object group, which never blocks a tile.
	 */
	WALL_DECORATION(1),

	/**
	 * The interactable object group, for objects that can be clicked and interacted with. TODO rename
	 */
	INTERACTABLE_OBJECT(2),

	/**
	 * The ground decoration object group, which may block a tile.
	 */
	GROUND_DECORATION(3);

	/**
	 * Gets the ObjectGroup with the specified integer value.
	 * 
	 * @param value The integer value of the ObjectGroup.
	 * @return The ObjectGroup.
	 * @throws IllegalArgumentException If there is no ObjectGroup with the specified value.
	 */
	public static ObjectGroup valueOf(int value) {
		return Arrays.stream(values()).filter(group -> group.value == value).findAny().orElseThrow(() -> new IllegalArgumentException("No ObjectGroup with a value of " + value + " exists."));
	}

	/**
	 * The integer value of the group.
	 */
	private final int value;

	/**
	 * Creates the ObjectGroup.
	 *
	 * @param value The integer value of the group.
	 */
	private ObjectGroup(int value) {
		this.value = value;
	}

	/**
	 * Gets the value of this ObjectGroup.
	 * 
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

}