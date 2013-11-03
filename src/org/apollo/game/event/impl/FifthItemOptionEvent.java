package org.apollo.game.event.impl;

/**
 * The fifth {@link ItemOptionEvent}, used mainly for dropping items.
 * @author Chris Fletcher
 */
public final class FifthItemOptionEvent extends ItemOptionEvent {

	/**
	 * Creates the fifth item option event.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public FifthItemOptionEvent(int interfaceId, int id, int slot) {
		super(5, interfaceId, id, slot);
	}

}
