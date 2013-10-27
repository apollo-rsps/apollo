package org.apollo.game.event.impl;

/**
 * The fifth {@link ItemActionEvent}.
 * @author Graham
 */
public final class FifthItemActionEvent extends ItemActionEvent {

	/**
	 * Creates the fifth item action event.
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FifthItemActionEvent(int interfaceId, int id, int slot) {
		super(5, interfaceId, id, slot);
	}

}
