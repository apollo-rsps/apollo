package org.apollo.game.message.impl;

/**
 * The second {@link ItemActionMessage}.
 * 
 * @author Graham
 */
public final class SecondItemActionMessage extends ItemActionMessage {

	/**
	 * Creates the second item action message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public SecondItemActionMessage(int interfaceId, int id, int slot) {
		super(2, interfaceId, id, slot);
	}

}