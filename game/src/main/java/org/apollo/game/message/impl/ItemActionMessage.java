package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

import java.util.OptionalInt;

/**
 * A {@link Message} sent by the client that represents some sort of action on an item. Note that the actual message
 * sent by the client is one of the five item action messages, but this is the message that should be intercepted (and
 * the option verified).
 *
 * @author Chris Fletcher
 */
public final class ItemActionMessage extends InventoryItemMessage {

	/**
	 * Creates the ItemActionMessage.
	 *
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ItemActionMessage(int option, int interfaceId, int id, int slot) {
		super(OptionalInt.of(option), interfaceId, id, slot);
	}

}