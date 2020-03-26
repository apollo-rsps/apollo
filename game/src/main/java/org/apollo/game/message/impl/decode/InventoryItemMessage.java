package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

import java.util.NoSuchElementException;
import java.util.OptionalInt;

/**
 * A {@link Message} that represents some sort of action on an item in an inventory.
 *
 * @author Khaled Abdeljaber
 */
public abstract class InventoryItemMessage extends Message {

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The component id.
	 */
	private final int componentId;

	/**
	 * The option number (1-5 if present).
	 */
	private final OptionalInt option;

	/**
	 * The item's slot.
	 */
	private final int slot;

	/**
	 * Creates the InventoryItemMessage.
	 *
	 * @param option      The option number, if applicable.
	 * @param interfaceId The interface id.
	 * @param id          The id.
	 * @param slot        The slot.
	 */
	protected InventoryItemMessage(OptionalInt option, int interfaceId, int componentId, int id, int slot) {
		this.option = option;
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.id = id;
		this.slot = slot;
	}

	/**
	 * Gets the item id.
	 *
	 * @return The item id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public final int getInterfaceId() {
		return interfaceId;
	}


	public int getComponentId() {
		return componentId;
	}

	/**
	 * Gets the option number.
	 *
	 * @return The option number.
	 * @throws NoSuchElementException If there is no option.
	 */
	public final int getOption() {
		return option.getAsInt();
	}

	/**
	 * Gets the slot.
	 *
	 * @return The slot.
	 */
	public final int getSlot() {
		return slot;
	}

	/**
	 * Returns whether or not this InventoryItemMessage has an option number.
	 *
	 * @return {@code true} iff this InventoryItemMessage has an option number.
	 */
	public final boolean hasOption() {
		return option.isPresent();
	}


	@Override
	public String toString() {
		return "InventoryItemMessage{" + "id=" + id + ", interfaceId=" + interfaceId + ", componentId=" + componentId + ", option=" + option + ", slot=" + slot + '}';
	}
}