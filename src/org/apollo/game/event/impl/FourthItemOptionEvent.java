package org.apollo.game.event.impl;

/**
 * The fourth {@link ItemOptionEvent}.
 * @author Chris Fletcher
 */
public final class FourthItemOptionEvent extends ItemOptionEvent {

	/**
	 * Creates the fourth item option event.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public FourthItemOptionEvent(int interfaceId, int id, int slot) {
		super(4, interfaceId, id, slot);
	}

}
