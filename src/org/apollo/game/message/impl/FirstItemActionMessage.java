package org.apollo.game.message.impl;

/**
 * The first {@link ItemActionMessage}.
 * 
 * @author Graham
 */
public final class FirstItemActionMessage extends ItemActionMessage {

	/**
	 * Creates the first item action message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FirstItemActionMessage(int interfaceId, int id, int slot) {
		super(1, interfaceId, id, slot);
	}

}