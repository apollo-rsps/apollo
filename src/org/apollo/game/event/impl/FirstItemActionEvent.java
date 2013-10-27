package org.apollo.game.event.impl;

/**
 * The first {@link ItemActionEvent}.
 * @author Graham
 */
public final class FirstItemActionEvent extends ItemActionEvent {

	/**
	 * Creates the first item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FirstItemActionEvent(int interfaceId, int id, int slot) {
		super(1, interfaceId, id, slot);
	}

}
