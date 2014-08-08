package org.apollo.game.message.impl;

/**
 * The fourth {@link ItemActionMessage}.
 * 
 * @author Graham
 */
public final class FourthItemActionMessage extends ItemActionMessage {

	/**
	 * Creates the fourth item action message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FourthItemActionMessage(int interfaceId, int id, int slot) {
		super(4, interfaceId, id, slot);
	}

}