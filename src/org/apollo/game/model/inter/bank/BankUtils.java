package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * Contains bank-related utility methods.
 * @author Graham
 */
public final class BankUtils {

	/**
	 * Opens a player's bank.
	 * @param player The player.
	 */
	public static void openBank(Player player) {
		InventoryListener invListener = new SynchronizationInventoryListener(player, BankConstants.SIDEBAR_INVENTORY_ID);
		InventoryListener bankListener = new SynchronizationInventoryListener(player, BankConstants.BANK_INVENTORY_ID);

		player.getInventory().addListener(invListener);
		player.getBank().addListener(bankListener);

		player.getInventory().forceRefresh();
		player.getBank().forceRefresh();

		InterfaceListener interListener = new BankInterfaceListener(player, invListener, bankListener);

		player.getInterfaceSet().openWindowWithSidebar(interListener, BankConstants.BANK_WINDOW_ID, BankConstants.SIDEBAR_ID);
	}

	/**
	 * Deposits an item into the player's bank.
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

		if (slot < 0 || slot >= inventory.capacity()) {
			return false;
		}

		Item item = inventory.get(slot);
		if (item.getId() != id) {
			return false;
		}

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
	 * Withdraws an item from a player's bank.
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

		if (slot < 0 || slot >= bank.capacity()) {
			return false;
		}

		Item item = bank.get(slot);
		if (item == null || item.getId() != id) {
			return false;
		}

		if (amount >= item.getAmount()) {
			amount = item.getAmount();
		}

		int newId = player.isWithdrawingNotes() ? ItemDefinition.itemToNote(item.getId()) : item.getId();

		if (inventory.freeSlots() == 0 && !(inventory.contains(newId) && ItemDefinition.forId(newId).isStackable())) {
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
	 * Default private constructor to prevent insantiation.
	 */
	private BankUtils() {

	}

}
