package org.apollo.game.event.impl;

/**
 * The second {@link ItemOptionEvent}, used for equipping an item (amongst others?).
 * @author Chris Fletcher
 */
public final class SecondItemOptionEvent extends ItemOptionEvent {

	/**
	 * Creates the second item option event.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public SecondItemOptionEvent(int interfaceId, int id, int slot) {
		super(2, interfaceId, id, slot);
	}

}
