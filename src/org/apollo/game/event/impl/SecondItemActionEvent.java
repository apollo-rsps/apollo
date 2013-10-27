package org.apollo.game.event.impl;

/**
 * The second {@link ItemActionEvent}.
 * @author Graham
 */
public final class SecondItemActionEvent extends ItemActionEvent {

	/**
	 * Creates the second item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public SecondItemActionEvent(int interfaceId, int id, int slot) {
		super(2, interfaceId, id, slot);
	}

}
