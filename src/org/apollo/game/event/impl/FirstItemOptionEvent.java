package org.apollo.game.event.impl;

/**
 * The first {@link ItemOptionEvent}, used for eating food or identifying herbs (amongst others).
 * @author Chris Fletcher
 */
public final class FirstItemOptionEvent extends ItemOptionEvent {

	/**
	 * Creates the first item option event.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public FirstItemOptionEvent(int interfaceId, int id, int slot) {
		super(1, interfaceId, id, slot);
	}

}
