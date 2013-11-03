package org.apollo.game.event.impl;

/**
 * The third {@link ItemOptionEvent}.
 * @author Chris Fletcher
 */
public final class ThirdItemOptionEvent extends ItemOptionEvent {

	/**
	 * Creates the third item option event.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ThirdItemOptionEvent(int interfaceId, int id, int slot) {
		super(3, interfaceId, id, slot);
	}

}
