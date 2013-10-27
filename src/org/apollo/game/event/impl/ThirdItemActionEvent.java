package org.apollo.game.event.impl;

/**
 * The third {@link ItemActionEvent}.
 * @author Graham
 */
public final class ThirdItemActionEvent extends ItemActionEvent {

	/**
	 * Creates the third item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public ThirdItemActionEvent(int interfaceId, int id, int slot) {
		super(3, interfaceId, id, slot);
	}

}
