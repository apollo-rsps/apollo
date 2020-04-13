package org.apollo.game.message.impl.decode;

import java.util.OptionalInt;

/**
 * An {@link InventoryItemMessage} sent by the client when an item's option is clicked (e.g. equip, eat, drink, etc).
 * Note that the actual message sent by the client is one of the five item option messages, but this is the message that
 * should be intercepted (and the option verified).
 *
 * @author Khaled Abdeljaber
 */
public final class ItemActionMessage extends InventoryItemMessage {

	/**
	 * Creates the ItemOptionMessage.
	 *
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ItemActionMessage(int option, int interfaceId, int componentId, int id, int slot) {
		super(OptionalInt.of(option), interfaceId, componentId, id, slot);
	}

	@Override
	public String toString() {
		return "ItemActionMessage{" + "id=" + id + ", interfaceId=" + interfaceId + ", componentId=" + componentId + ", option=" + option + ", slot=" + slot + '}';
	}
}