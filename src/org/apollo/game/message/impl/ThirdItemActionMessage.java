package org.apollo.game.message.impl;

/**
 * The third {@link ItemActionMessage}.
 * 
 * @author Graham
 */
public final class ThirdItemActionMessage extends ItemActionMessage {

	/**
	 * Creates the third item action message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public ThirdItemActionMessage(int interfaceId, int id, int slot) {
		super(3, interfaceId, id, slot);
	}

}