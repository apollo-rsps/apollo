package org.apollo.game.message.handler;

import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.ItemOptionMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.game.model.inv.Inventory;
import org.apollo.util.LanguageUtil;

import static org.apollo.game.model.entity.EquipmentConstants.SHIELD;
import static org.apollo.game.model.entity.EquipmentConstants.WEAPON;
import static org.apollo.game.model.inv.SynchronizationInventoryListener.INVENTORY_ID;

/**
 * A {@link MessageHandler} that equips items.
 *
 * @author Major
 * @author Graham
 * @author Ryley
 */
public final class EquipItemHandler extends MessageHandler<ItemOptionMessage> {

	/**
	 * The option used when equipping an item.
	 */
	private static final int EQUIP_OPTION = 2;

	/**
	 * Creates the EquipItemHandler.
	 *
	 * @param world The {@link World} the {@link ItemOptionMessage} occurred in.
	 */
	public EquipItemHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ItemOptionMessage message) {
		if (message.getInterfaceId() != INVENTORY_ID || message.getOption() != EQUIP_OPTION) {
			return;
		}

		int inventorySlot = message.getSlot();
		Inventory inventory = player.getInventory();

		Item equipping = inventory.get(inventorySlot);
		int equippingId = equipping.getId();

		EquipmentDefinition definition = EquipmentDefinition.lookup(equippingId);

		if (definition == null) {
			return;
		} else if (!hasRequirements(player, definition)) {
			message.terminate();
			return;
		}

		Inventory equipment = player.getEquipment();

		Item weapon = equipment.get(WEAPON);
		Item shield = equipment.get(SHIELD);

		if (definition.isTwoHanded()) {
			handleTwoHanded(inventory, equipment, inventorySlot, weapon, shield, player, message);
			return;
		}

		int equipmentSlot = definition.getSlot();

		if (equipmentSlot == SHIELD && weapon != null && EquipmentDefinition.lookup(weapon.getId()).isTwoHanded()) {
			equipment.set(SHIELD, inventory.reset(inventorySlot));
			inventory.add(equipment.reset(WEAPON));
			return;
		}

		Item current = equipment.get(equipmentSlot);

		if (current != null && current.getId() == equippingId && current.getDefinition().isStackable()) {
			long total = (long) current.getAmount() + equipping.getAmount();

			if (total <= Integer.MAX_VALUE) {
				equipment.set(equipmentSlot, new Item(equippingId, (int) total));
				inventory.set(inventorySlot, null);
			} else {
				equipment.set(equipmentSlot, new Item(equippingId, Integer.MAX_VALUE));
				inventory.set(inventorySlot, new Item(equippingId, (int) (total - Integer.MAX_VALUE)));
			}
		} else {
			inventory.set(inventorySlot, null);
			equipment.set(equipmentSlot, equipping);

			if (current != null) {
				inventory.set(inventorySlot, current);
			}
		}
	}

	/**
	 * Attempts to equip the two-handed item requested by the {@link Player}.
	 *
	 * @param inventory The {@link Inventory} of the {@link Player}.
	 * @param equipment The equipment of the {@link Player}.
	 * @param inventorySlot The slot of the 2-handed item in the {@link Inventory}.
	 * @param weapon The weapon the {@link Player} currently has equipped.
	 * @param shield The shield the {@link Player} currently has equipped.
	 * @param player The {@link Player} equipping the item.
	 * @param message The {@link ItemOptionMessage} sent by the client.
	 */
	private void handleTwoHanded(Inventory inventory, Inventory equipment, int inventorySlot, Item weapon, Item shield, Player player, ItemOptionMessage message) {
		int slotsRequired = weapon != null && shield != null ? 1 : 0;

		if (inventory.freeSlots() < slotsRequired) {
			player.sendMessage("You don't have enough free inventory space to do that.");
			message.terminate();
			return;
		}

		equipment.reset(SHIELD);
		equipment.set(WEAPON, inventory.reset(inventorySlot));
		
		if (shield != null) {
			inventory.add(shield);
		}

		if (weapon != null) {
			inventory.add(weapon);
		}

		player.stopAction();
	}


	/**
	 * Returns whether or not the specified {@link Player} has the required levels to equip the item with the specified
	 * {@link EquipmentDefinition}, sending the {@link Player} a message if not.
	 *
	 * @param player The {@link Player} trying to equip the item. Must not be {@code null}.
	 * @param definition The {@link EquipmentDefinition} of the item being equipped. Must not be {@code null}.
	 * @return {@code true} iff the {@link Player} meets all of the skill requirements for the item.
	 */
	private boolean hasRequirements(Player player, EquipmentDefinition definition) {
		SkillSet skills = player.getSkillSet();

		for (int id = Skill.ATTACK; id <= Skill.MAGIC; id++) {
			int requirement = definition.getLevel(id);

			if (skills.getMaximumLevel(id) < requirement) {
				String name = Skill.getName(id);

				player.sendMessage("You are not a high enough level to use this item.");
				player.sendMessage("You need to have " + LanguageUtil.getIndefiniteArticle(name) + " " + name.toLowerCase() + " level of " + requirement + "." );
				return false;
			}
		}
		return true;
	}
}