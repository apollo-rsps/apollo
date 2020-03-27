package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player clicks a button.
 *
 * @author Khaled Abdeljaber
 */
public class IfActionMessage extends Message {

	/**
	 * The option that was pressed.
	 */
	private final int action;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The component id.
	 */
	private final int componentId;

	/**
	 * The slot of the item.
	 */
	private final int slot;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * Instantiates a new If action message.
	 *
	 * @param action      the action
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param slot        the slot
	 * @param itemId      the item id
	 */
	public IfActionMessage(int action, int interfaceId, int componentId, int slot, int itemId) {
		this.action = action;
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.slot = slot;
		this.itemId = itemId;
	}

	/**
	 * Gets action.
	 *
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Gets interface id.
	 *
	 * @return the interface id
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets component id.
	 *
	 * @return the component id
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Gets slot.
	 *
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets item id.
	 *
	 * @return the item id
	 */
	public int getItemId() {
		return itemId;
	}

	public int getId() {
		return interfaceId << 16 | componentId;
	}


	@Override
	public String toString() {
		return "IfActionMessage{" + "action=" + action + ", interfaceId=" + interfaceId + ", componentId=" + componentId + ", slot=" + slot + ", itemId=" + itemId + '}';
	}
}