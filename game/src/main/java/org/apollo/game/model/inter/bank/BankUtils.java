package org.apollo.game.model.inter.bank;

import org.apollo.cache.def.ItemDefinition;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inv.Inventory;

/**
 * Contains bank-related utility methods.
 *
 * @author Graham
 */
public final class BankUtils {

	/**
	 * Deposits an item into the player's bank.
	 *
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean deposit(Player player, int slot, int id, int amount) {
		if (amount == 0) {
			return true;
		}

		Inventory inventory = player.getInventory();
		Inventory bank = player.getBank();

		Item item = inventory.get(slot);
		int newId = ItemDefinition.noteToItem(item.getId());

		if (bank.freeSlots() == 0 && !bank.contains(item.getId())) {
			bank.forceCapacityExceeded();
			return true;
		}

		int removed;
		if (amount > 1) {
			inventory.stopFiringEvents();
		}
		try {
			removed = inventory.remove(item.getId(), amount);
		} finally {
			if (amount > 1) {
				inventory.startFiringEvents();
			}
		}
		if (amount > 1) {
			inventory.forceRefresh();
		}
		bank.add(newId, removed);

		return true;
	}

	/**
	 * Opens a player's bank.
	 *
	 * @param player The player.
	 */
	public static void openBank(Player player) {
		// Required for access within plugin Actions.
		player.openBank();
	}

	/**
	 * Withdraws an item from a player's bank.
	 *
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 * @param amount The amount.
	 * @return {@code false} if the chain should be broken.
	 */
	public static boolean withdraw(Player player, int slot, int id, int amount) {
		if (amount == 0) {
			return true;
		}

		Inventory inventory = player.getInventory();
		Inventory bank = player.getBank();

		Item item = bank.get(slot);

		if (amount >= item.getAmount()) {
			amount = item.getAmount();
		}

		int newId = player.isWithdrawingNotes() ? ItemDefinition.itemToNote(item.getId()) : item.getId();

		if (inventory.freeSlots() == 0 && !(inventory.contains(newId) && ItemDefinition.lookup(newId).isStackable())) {
			inventory.forceCapacityExceeded();
			return true;
		}

		int remaining = inventory.add(newId, amount);

		bank.stopFiringEvents();
		try {
			bank.remove(item.getId(), amount - remaining);
			bank.shift();
		} finally {
			bank.startFiringEvents();
		}

		bank.forceRefresh();
		return true;
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private BankUtils() {

	}

}