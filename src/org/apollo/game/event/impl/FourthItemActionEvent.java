package org.apollo.game.event.impl;

/**
 * The fourth {@link ItemActionEvent}.
 * @author Graham
 */
public final class FourthItemActionEvent extends ItemActionEvent {

	/**
	 * Creates the fourth item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FourthItemActionEvent(int interfaceId, int id, int slot) {
		super(4, interfaceId, id, slot);
	}

}
