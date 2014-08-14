package org.apollo.game.model.inv;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.Item;
import org.apollo.game.model.def.ItemDefinition;

/**
 * Represents an inventory - a collection of {@link Item}s.
 * 
 * @author Graham
 */
public final class Inventory implements Cloneable {

	/**
	 * An enumeration containing the different 'stacking modes' of an {@link Inventory}.
	 * 
	 * @author Graham
	 */
	public enum StackMode {

		/**
		 * When in {@link #STACK_ALWAYS} mode, an {@link Inventory} will stack every single item, regardless of the
		 * settings of individual items.
		 */
		STACK_ALWAYS,

		/**
		 * When in {@link #STACK_NEVER} mode, an {@link Inventory} will never stack items.
		 */
		STACK_NEVER,

		/**
		 * When in {@link #STACK_STACKABLE_ITEMS} mode, an {@link Inventory} will stack items depending on their
		 * settings.
		 */
		STACK_STACKABLE_ITEMS;

	}

	/**
	 * The capacity of this inventory.
	 */
	private final int capacity;

	/**
	 * A flag indicating if events are being fired.
	 */
	private boolean firingEvents = true; // TODO: make this reentrant

	/**
	 * The items in this inventory.
	 */
	private Item[] items;

	/**
	 * A list of inventory listeners.
	 */
	private final List<InventoryListener> listeners = new ArrayList<>();

	/**
	 * The stacking mode.
	 */
	private final StackMode mode;

	/**
	 * The size of this inventory - the number of 'used slots'.
	 */
	private int size = 0;

	/**
	 * Creates an inventory.
	 * 
	 * @param capacity The capacity.
	 */
	public Inventory(int capacity) {
		this(capacity, StackMode.STACK_STACKABLE_ITEMS);
	}

	/**
	 * Creates an inventory.
	 * 
	 * @param capacity The capacity.
	 * @param mode The {@link StackMode}.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @throws NullPointerException If the mode is {@code null}.
	 */
	public Inventory(int capacity, StackMode mode) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity cannot be negative.");
		} else if (mode == null) {
			throw new NullPointerException("Stacking mode cannot be null.");
		}
		items = new Item[this.capacity = capacity];
		this.mode = mode;
	}

	/**
	 * An alias for {@code add(id, 1)}.
	 * 
	 * @param id The id.
	 * @return {@code true} if the item was added, {@code false} if there was not enough room.
	 */
	public boolean add(int id) {
		return add(id, 1) == 0;
	}

	/**
	 * An alias for {@code add(new Item(id, amount)}.
	 * 
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that remains.
	 */
	public int add(int id, int amount) {
		Item item = add(new Item(id, amount));
		return item != null ? item.getAmount() : 0;
	}

	/**
	 * Adds an item to this inventory. This will attempt to add as much of the item that is possible. If the item
	 * remains, it will be returned (in the case of stackable items, any quantity that remains in the stack is
	 * returned). If nothing remains, the method will return {@code null}. If something remains, the listener will also
	 * be notified which could be used, for example, to send a message to the player.
	 * 
	 * @param item The item to add to this inventory.
	 * @return The item that remains if there is not enough room in the inventory. If nothing remains, {@code null}.
	 */
	public Item add(Item item) {
		int id = item.getId();
		boolean stackable = isStackable(item.getDefinition());
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				Item other = items[slot];
				if (other != null && other.getId() == id) {
					long total = item.getAmount() + other.getAmount();
					int amount, remaining;
					if (total > Integer.MAX_VALUE) {
						amount = (int) (total - Integer.MAX_VALUE);
						remaining = (int) (total - amount);
						notifyCapacityExceeded();
					} else {
						amount = (int) total;
						remaining = 0;
					}
					set(slot, new Item(id, amount));
					return remaining > 0 ? new Item(id, remaining) : null;
				}
			}
			for (int slot = 0; slot < capacity; slot++) {
				Item other = items[slot];
				if (other == null) {
					set(slot, item);
					return null;
				}
			}
			notifyCapacityExceeded();
			return item;
		}

		int remaining = item.getAmount();

		stopFiringEvents();
		try {
			Item single = new Item(item.getId(), 1);
			for (int slot = 0; slot < capacity; slot++) {
				if (items[slot] == null) {
					remaining--;
					set(slot, single); // share the instances
					if (remaining <= 0) {
						break;
					}
				}
			}
		} finally {
			startFiringEvents();
		}

		if (remaining != item.getAmount()) {
			notifyItemsUpdated();
		}
		if (remaining > 0) {
			notifyCapacityExceeded();
		}

		return new Item(item.getId(), remaining);
	}

	/**
	 * Adds an {@link InventoryListener}.
	 * 
	 * @param listener The listener.
	 */
	public void addListener(InventoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Gets the capacity of this inventory.
	 * 
	 * @return The capacity.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Checks the bounds of the specified slot.
	 * 
	 * @param slot The slot.
	 * @throws IndexOutOfBoundsException If the slot is out of bounds.
	 */
	private void checkBounds(int slot) {
		if (slot < 0 || slot >= capacity) {
			throw new IndexOutOfBoundsException("Slot out of bounds.");
		}
	}

	/**
	 * Clears the inventory.
	 */
	public void clear() {
		items = new Item[capacity];
		size = 0;
		notifyItemsUpdated();
	}

	/**
	 * Creates a copy of this inventory. Listeners are not copied, they must be added again yourself! This is so cloned
	 * copies don't send updates to their counterparts.
	 */
	@Override
	public Inventory clone() {
		Inventory copy = new Inventory(capacity, mode);
		System.arraycopy(items, 0, copy.items, 0, capacity);
		copy.size = size;
		return copy;
	}

	/**
	 * Checks if this inventory contains an item with the specified id.
	 * 
	 * @param id The item's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return slotOf(id) != -1;
	}

	/**
	 * Forces the capacity to exceeded event to be fired.
	 */
	public void forceCapacityExceeded() {
		notifyCapacityExceeded();
	}

	/**
	 * Forces the refresh of this inventory.
	 */
	public void forceRefresh() {
		notifyItemsUpdated();
	}

	/**
	 * Forces a refresh of a specific slot.
	 * 
	 * @param slot The slot.
	 */
	public void forceRefresh(int slot) {
		notifyItemUpdated(slot);
	}

	/**
	 * Gets the number of free slots.
	 * 
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size;
	}

	/**
	 * Gets the item in the specified slot.
	 * 
	 * @param slot The slot.
	 * @return The item, or {@code null} if the slot is empty.
	 */
	public Item get(int slot) {
		checkBounds(slot);
		return items[slot];
	}

	/**
	 * Gets the amount of items with the specified id in this inventory.
	 * 
	 * @param id The id.
	 * @return The number of matching items, or {@code 0} if none were found.
	 */
	public int getAmount(int id) {
		if (isStackable(ItemDefinition.lookup(id))) {
			int slot = slotOf(id);
			if (slot != -1) {
				return items[slot].getAmount();
			}
			return 0;
		}

		int amount = 0, usedSlots = 0;
		for (int i = 0; i < capacity && usedSlots <= size; i++) {
			Item item = items[i];
			if (item != null) {
				if (item.getId() == id) {
					amount++;
				}
				usedSlots++;
			}
		}
		return amount;
	}

	/**
	 * Gets a clone of the items array.
	 * 
	 * @return A clone of the items array.
	 */
	public Item[] getItems() {
		return items.clone();
	}

	/**
	 * Checks if the item with the specified {@link ItemDefinition} should be stacked.
	 * 
	 * @param definition The item definition.
	 * @return {@code true} if the item should be stacked, {@code false} otherwise.
	 */
	private boolean isStackable(ItemDefinition definition) {
		if (mode == StackMode.STACK_ALWAYS) {
			return true;
		} else if (mode == StackMode.STACK_STACKABLE_ITEMS) {
			return definition.isStackable();
		}
		return false;
	}

	/**
	 * Notifies listeners that the capacity of this inventory has been exceeded.
	 */
	private void notifyCapacityExceeded() {
		if (firingEvents) {
			for (InventoryListener listener : listeners) {
				listener.capacityExceeded(this);
			}
		}
	}

	/**
	 * Notifies listeners that all the items have been updated.
	 */
	private void notifyItemsUpdated() {
		if (firingEvents) {
			for (InventoryListener listener : listeners) {
				listener.itemsUpdated(this);
			}
		}
	}

	/**
	 * Notifies listeners that the specified slot has been updated.
	 * 
	 * @param slot The slot.
	 */
	private void notifyItemUpdated(int slot) {
		if (firingEvents) {
			for (InventoryListener listener : listeners) {
				listener.itemUpdated(this, slot, items[slot]);
			}
		}
	}

	/**
	 * Removes one item with the specified id.
	 * 
	 * @param id The id.
	 * @return {@code true} if the item was removed, {@code false} otherwise.
	 */
	public boolean remove(int id) {
		return remove(id, 1) == 1;
	}

	/**
	 * Removes {@code amount} of the item with the specified {@code id}. If the item is stackable, it will remove it
	 * from the stack. If not, it'll remove {@code amount} items.
	 * 
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that was removed.
	 */
	public int remove(int id, int amount) {
		ItemDefinition definition = ItemDefinition.lookup(id);
		boolean stackable = isStackable(definition);
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				Item item = items[slot];
				if (item != null && item.getId() == id) {
					if (amount >= item.getAmount()) {
						set(slot, null);
						return item.getAmount();
					}
					int newAmount = item.getAmount() - amount;
					set(slot, new Item(item.getId(), newAmount));
					return amount;
				}
			}
			return 0;
		}
		int removed = 0;
		for (int slot = 0; slot < capacity; slot++) {
			Item item = items[slot];
			if (item != null && item.getId() == id) {
				set(slot, null);
				removed++;
			}
			if (removed >= amount) {
				break;
			}
		}
		return removed;
	}

	/**
	 * An alias for {@code remove(item.getId(), item.getAmount())}.
	 * 
	 * @param item The item to remove.
	 * @return The amount that was removed.
	 */
	public int remove(Item item) {
		return remove(item.getId(), item.getAmount());
	}

	/**
	 * Removes all the listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(InventoryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes {@code amount} of the item at the specified {@code slot}. If the item is not stacked, it will only remove
	 * the single item at the slot (meaning it will ignore any amount higher than 1). This means that this method will
	 * under no circumstances make any changes to other slots.
	 * 
	 * @param slot The slot.
	 * @param id The item id.
	 * @param amount The amount to remove.
	 * @return The amount that was removed (0 if nothing was removed).
	 */
	public int removeSlot(int slot, int amount) {
		if (amount == 0) {
			return 0;
		}

		Item item = items[slot];
		if (item != null) {
			int itemAmount = item.getAmount();
			int removed = itemAmount;
			if (removed > amount) {
				removed = amount;
			}

			int remainder = itemAmount - removed;
			set(slot, remainder > 0 ? new Item(item.getId(), remainder) : null);

			return removed;
		}
		return 0;
	}

	/**
	 * Removes the item (if any) that is in the specified slot.
	 * 
	 * @param slot
	 * @return The item that was in the slot.
	 */
	public Item reset(int slot) {
		checkBounds(slot);

		Item old = items[slot];
		if (old != null) {
			size--;
		}
		items[slot] = null;
		notifyItemUpdated(slot);
		return old;
	}

	/**
	 * Sets the item that is in the specified slot.
	 * 
	 * @param slot The slot.
	 * @param item The item, or {@code null} to remove the item that is in the slot.
	 * @return The item that was in the slot.
	 */
	public Item set(int slot, Item item) {
		if (item == null) {
			return reset(slot);
		}
		checkBounds(slot);

		Item old = items[slot];
		if (old == null) {
			size++;
		}
		items[slot] = item;
		notifyItemUpdated(slot);
		return old;
	}

	/**
	 * Shifts all items to the top left of the container, leaving no gaps.
	 */
	public void shift() {
		Item[] old = items;
		items = new Item[capacity];
		for (int slot = 0, pos = 0; slot < items.length; slot++) {
			if (old[slot] != null) {
				items[pos++] = old[slot];
			}
		}

		if (firingEvents) {
			notifyItemsUpdated();
		}
	}

	/**
	 * Gets the size of this inventory - the number of used slots.
	 * 
	 * @return The size.
	 */
	public int size() {
		return size;
	}

	/**
	 * Gets the inventory slot for the specified id.
	 * 
	 * @param id The id.
	 * @return The first slot containing the specified item, or {@code -1} if none of the slots matched the conditions.
	 */
	public int slotOf(int id) {
		int usedSlots = 0;
		for (int slot = 0; slot < capacity && usedSlots <= size; slot++) {
			Item item = items[slot];
			if (item != null) {
				if (item.getId() == id) {
					return slot;
				}
				usedSlots++;
			}
		}
		return -1;
	}

	/**
	 * Starts the firing of events.
	 */
	public void startFiringEvents() {
		firingEvents = true;
	}

	/**
	 * Stops the firing of events.
	 */
	public void stopFiringEvents() {
		firingEvents = false;
	}

	/**
	 * Swaps the two items at the specified slots.
	 * 
	 * @param insert If the swap should be done in insertion mode.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 */
	public void swap(boolean insert, int oldSlot, int newSlot) {
		checkBounds(oldSlot);
		checkBounds(newSlot);

		if (insert) {
			if (newSlot > oldSlot) {
				for (int slot = oldSlot; slot < newSlot; slot++) {
					swap(slot, slot + 1);
				}
			} else if (oldSlot > newSlot) {
				for (int slot = oldSlot; slot > newSlot; slot--) {
					swap(slot, slot - 1);
				}
			}
			forceRefresh();
		} else {
			Item item = items[oldSlot];
			items[oldSlot] = items[newSlot];
			items[newSlot] = item;
			notifyItemUpdated(oldSlot);
			notifyItemUpdated(newSlot);
		}
	}

	/**
	 * Swaps the two items at the specified slots.
	 * 
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 */
	public void swap(int oldSlot, int newSlot) {
		swap(false, oldSlot, newSlot);
	}

}