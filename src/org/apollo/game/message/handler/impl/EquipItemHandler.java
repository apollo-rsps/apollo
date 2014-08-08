package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ItemOptionMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.util.LanguageUtil;

/**
 * A {@link MessageHandler} that equips items.
 * 
 * @author Major
 * @author Graham
 */
public final class EquipItemHandler extends MessageHandler<ItemOptionMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ItemOptionMessage message) {
		if (message.getOption() != 2 || message.getInterfaceId() != SynchronizationInventoryListener.INVENTORY_ID) {
			return;
		}

		int inventorySlot = message.getSlot();
		Item equipping = player.getInventory().get(inventorySlot);
		int equippingId = equipping.getId();
		EquipmentDefinition definition = EquipmentDefinition.lookup(equippingId);

		if (definition == null) {
			// We don't break the chain here or any item option messages won't work!
			return;
		}

		for (int id = 0; id < 5; id++) {
			int requirement = definition.getLevel(id);

			if (player.getSkillSet().getSkill(id).getMaximumLevel() < requirement) {
				String skillName = Skill.getName(id);
				String article = LanguageUtil.getIndefiniteArticle(skillName);

				player.sendMessage("You need " + article + " " + skillName + " level of " + requirement
						+ " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
		}

		Inventory inventory = player.getInventory();
		Inventory equipment = player.getEquipment();

		int equipmentSlot = definition.getSlot();
		Item currentlyEquipped = equipment.get(equipmentSlot);

		if (equipping.getDefinition().isStackable()
				&& (currentlyEquipped == null || currentlyEquipped.getId() == equippingId)) {
			equipment.set(definition.getSlot(), equipping);
			inventory.reset(inventorySlot);
			return;
		}

		Item weapon = equipment.get(EquipmentConstants.WEAPON);
		Item shield = equipment.get(EquipmentConstants.SHIELD);

		if (definition.isTwoHanded()) {
			int slotsRequired = weapon != null ? shield != null ? 1 : 0 : 0;
			if (inventory.freeSlots() < slotsRequired) {
				ctx.breakHandlerChain();
				return;
			}

			equipment.reset(EquipmentConstants.WEAPON);
			equipment.reset(EquipmentConstants.SHIELD);
			equipment.set(EquipmentConstants.WEAPON, inventory.reset(inventorySlot));

			if (shield != null) {
				inventory.add(shield);
			}
			if (weapon != null) {
				inventory.add(weapon);
			}
		} else if (definition.getSlot() == EquipmentConstants.SHIELD && weapon != null
				&& EquipmentDefinition.lookup(weapon.getId()).isTwoHanded()) {
			equipment.set(EquipmentConstants.SHIELD, inventory.reset(inventorySlot));
			inventory.add(equipment.reset(EquipmentConstants.WEAPON));
		} else {
			Item previous = equipment.reset(equipmentSlot);
			inventory.remove(equipping); // no need for fancy stuff here as we know the item isn't stackable.
			equipment.set(equipmentSlot, equipping);
			if (previous != null) {
				inventory.add(previous);
			}
		}
	}

}