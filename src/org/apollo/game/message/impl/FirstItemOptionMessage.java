package org.apollo.game.message.impl;

/**
 * The first {@link ItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
public final class FirstItemOptionMessage extends ItemOptionMessage {

	/**
	 * Creates the first item option message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public FirstItemOptionMessage(int interfaceId, int id, int slot) {
		super(1, interfaceId, id, slot);
	}

}