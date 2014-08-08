package org.apollo.game.message.impl;

/**
 * The fifth {@link ItemActionMessage}.
 * 
 * @author Graham
 */
public final class FifthItemActionMessage extends ItemActionMessage {

	/**
	 * Creates the fifth item action message.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 */
	public FifthItemActionMessage(int interfaceId, int id, int slot) {
		super(5, interfaceId, id, slot);
	}

}