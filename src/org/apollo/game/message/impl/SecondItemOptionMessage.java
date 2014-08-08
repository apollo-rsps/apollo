package org.apollo.game.message.impl;

/**
 * The second {@link ItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
public final class SecondItemOptionMessage extends ItemOptionMessage {

	/**
	 * Creates the second item option message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public SecondItemOptionMessage(int interfaceId, int id, int slot) {
		super(2, interfaceId, id, slot);
	}

}