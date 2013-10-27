package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inv.InventoryListener;

/**
 * Represents an inventory - a collection of {@link Item}s.
 * @author Graham
 */
public final class Inventory implements Cloneable {

	/**
	 * An enumeration containing the different 'stacking modes' of an
	 * {@link Inventory}.
	 * @author Graham
	 */
	public enum StackMode {

		/**
		 * When in {@link #STACK_ALWAYS} mode, an {@link Inventory} will stack
		 * every single item, regardless of the settings of individual items.
		 */
		STACK_ALWAYS,

		/**
		 * When in {@link #STACK_STACKABLE_ITEMS} mode, an {@link Inventory}
		 * will stack items depending on their settings.
		 */
		STACK_STACKABLE_ITEMS,

		/**
		 * When in {@link #STACK_NEVER} mode, an {@link Inventory} will never
		 * stack items.
		 */
		STACK_NEVER;

	}

	/**
	 * A list of inventory listeners.
	 */
	private final List<InventoryListener> listeners = new ArrayList<InventoryListener>();

	/**
	 * The capacity of this inventory.
	 */
	private final int capacity;

	/**
	 * The items in this inventory.
	 */
	private Item[] items;

	/**
	 * The stacking mode.
	 */
	private final StackMode mode;

	/**
	 * The size of this inventory - the number of 'used slots'.
	 */
	private int size = 0;

	/**
	 * A flag indicating if events are being fired.
	 */
	private boolean firingEvents = true; // TODO: make this reentrant

	/**
	 * Creates an inventory.
	 * @param capacity The capacity.
	 * @throws IllegalArgumentException if the capacity is negative.
	 */
	public Inventory(int capacity) {
		this(capacity, StackMode.STACK_STACKABLE_ITEMS);
	}

	/**
	 * Creates an inventory.
	 * @param capacity The capacity.
	 * @param mode The stacking mode.
	 * @throws IllegalArgumentException if the capacity is negative.
	 * @throws NullPointerException if the mode is {@code null}.
	 */
	public Inventory(int capacity, StackMode mode) {
		if (capacity < 0) {
			throw new IllegalArgumentException("capacity cannot be negative");
		}
		if (mode == null) {
			throw new NullPointerException("mode");
		}
		this.capacity = capacity;
		this.items = new Item[capacity];
		this.mode = mode;
	}

	/**
	 * Creates a copy of this inventory. Listeners are not copied, they must be
	 * added again yourself! This is so cloned copies don't send updates to
	 * their counterparts.
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
	 * @param id The item's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		for (int i = 0; i < capacity; i++) {
			Item item = items[i];
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the number of free slots.
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size;
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
	 * Gets the capacity of this inventory.
	 * @return The capacity.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Gets the size of this inventory - the number of used slots.
	 * @return The size.
	 */
	public int size() {
		return size;
	}

	/**
	 * Gets the item in the specified slot.
	 * @param slot The slot.
	 * @return The item, or {@code null} if the slot is empty.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public Item get(int slot) {
		checkBounds(slot);
		return items[slot];
	}

	/**
	 * Sets the item that is in the specified slot.
	 * @param slot The slot.
	 * @param item The item, or {@code null} to remove the item that is in the
	 * slot.
	 * @return The item that was in the slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
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
	 * Removes the item (if any) that is in the specified slot.
	 * @param slot
	 * @return The item that was in the slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
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
	 * An alias for {@code add(id, 1)}.
	 * @param id The id.
	 * @return {@code true} if the item was added, {@code false} if there was
	 * not enough room.
	 */
	public boolean add(int id) {
		return add(id, 1) == 0;
	}

	/**
	 * An alias for {@code add(new Item(id, amount)}.
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that remains.
	 */
	public int add(int id, int amount) {
		Item item = add(new Item(id, amount));
		if (item != null) {
			return item.getAmount();
		}
		return 0;
	}

	/**
	 * Adds an item to this inventory. This will attempt to add as much of the
	 * item that is possible. If the item remains, it will be returned (in the
	 * case of stackable items, any quantity that remains in the stack is
	 * returned). If nothing remains, the method will return {@code null}. If
	 * something remains, the listener will also be notified which could be
	 * used, for example, to send a message to the player.
	 * @param item The item to add to this inventory.
	 * @return The item that remains if there is not enough room in the
	 * inventory. If nothing remains, {@code null}.
	 */
	public Item add(Item item) {
		int id = item.getId();
		boolean stackable = isStackable(item.getDefinition());
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				Item other = items[slot];
				if (other != null && other.getId() == id) {
					long total = item.getAmount() + other.getAmount();
					int amount;
					int remaining;
					if (total > Integer.MAX_VALUE) {
						amount = (int) (total - Integer.MAX_VALUE);
						remaining = (int) (total - amount);
						notifyCapacityExceeded();
					} else {
						amount = (int) total;
						remaining = 0;
					}
					set(slot, new Item(id, amount));
					return remaining > 0 ? new Item(id, remaining): null;
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
	 * Removes one item with the specified id.
	 * @param id The id.
	 * @return {@code true} if the item was removed, {@code false} otherwise.
	 */
	public boolean remove(int id) {
		return remove(id, 1) == 1;
	}

	/**
	 * An alias for {@code remove(item.getId(), item.getAmount())}.
	 * @param item The item to remove.
	 * @return The amount that was removed.
	 */
	public int remove(Item item) {
		return remove(item.getId(), item.getAmount());
	}

	/**
	 * Removes {@code amount} of the item with the specified {@code id}. If the
	 * item is stackable, it will remove it from the stack. If not, it'll
	 * remove {@code amount} items.
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that was removed.
	 */
	public int remove(int id, int amount) {
		ItemDefinition def = ItemDefinition.forId(id);
		boolean stackable = isStackable(def);
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				Item item = items[slot];
				if (item != null && item.getId() == id) {
					if (amount >= item.getAmount()) {
						set(slot, null);
						return item.getAmount();
					} else {
						int newAmount = item.getAmount() - amount;
						set(slot, new Item(item.getId(), newAmount));
						return amount;
					}
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
	 * Shifts all items to the top left of the container, leaving no gaps.
	 */
	public void shift() {
		Item[] old = items;
		items = new Item[capacity];
		for (int i = 0, pos = 0; i < items.length; i++) {
			if (old[i] != null) {
				items[pos++] = old[i];
			}
		}
		if (firingEvents) {
			notifyItemsUpdated();
		}
	}

	/**
	 * Swaps the two items at the specified slots.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 * @throws IndexOutOufBoundsException if the slot is out of bounds.
	 */
	public void swap(int oldSlot, int newSlot) {
		swap(false, oldSlot, newSlot);
	}

	/**
	 * Swaps the two items at the specified slots.
	 * @param insert If the swap should be done in insertion mode.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
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
			} // else no change is required - aren't we lucky?
			forceRefresh();
		} else {
			Item temp = items[oldSlot];
			items[oldSlot] = items[newSlot];
			items[newSlot] = temp;
			notifyItemsUpdated(); // TODO can we just fire for the two slots?
		}
	}

	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addListener(InventoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener(InventoryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes all the listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Notifies listeners that the capacity of this inventory has been
	 * exceeded.
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
	 * @param slot The slot.
	 */
	private void notifyItemUpdated(int slot) {
		if (firingEvents) {
			Item item = items[slot];
			for (InventoryListener listener : listeners) {
				listener.itemUpdated(this, slot, item);
			}
		}
	}

	/**
	 * Checks the bounds of the specified slot.
	 * @param slot The slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	private void checkBounds(int slot) {
		if (slot < 0 || slot >= capacity) {
			throw new IndexOutOfBoundsException("slot out of bounds");
		}
	}

	/**
	 * Checks if the item specified by the definition should be stacked.
	 * @param def The definition.
	 * @return {@code true} if the item should be stacked, {@code false}
	 * otherwise.
	 */
	private boolean isStackable(ItemDefinition def) {
		if (mode == StackMode.STACK_ALWAYS) {
			return true;
		} else if (mode == StackMode.STACK_STACKABLE_ITEMS) {
			return def.isStackable();
		} else { // will be STACK_NEVER
			return false;
		}
	}

	/**
	 * Gets a clone of the items array.
	 * @return A clone of the items array.
	 */
	public Item[] getItems() {
		return items.clone();
	}

	/**
	 * Stops the firing of events.
	 */
	public void stopFiringEvents() {
		firingEvents = false;
	}

	/**
	 * Starts the firing of events.
	 */
	public void startFiringEvents() {
		firingEvents = true;
	}

	/**
	 * Forces the refresh of this inventory.
	 */
	public void forceRefresh() {
		notifyItemsUpdated();
	}

	/**
	 * Forces a refresh of a specific slot.
	 * @param slot The slot.
	 */
	public void forceRefresh(int slot) {
		notifyItemUpdated(slot);
	}

	/**
	 * Forces the capacity to exceeded event to be fired.
	 */
	public void forceCapacityExceeded() {
		notifyCapacityExceeded();
	}

}
