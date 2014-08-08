package org.apollo.game.message.impl;

/**
 * The third {@link ItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
public final class ThirdItemOptionMessage extends ItemOptionMessage {

	/**
	 * Creates the third item option message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ThirdItemOptionMessage(int interfaceId, int id, int slot) {
		super(3, interfaceId, id, slot);
	}

}