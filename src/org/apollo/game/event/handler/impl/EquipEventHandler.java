package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOptionEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.util.LanguageUtil;

/**
 * An {@link EventHandler} that equips items.
 * 
 * @author Major
 * @author Graham
 */
public final class EquipEventHandler extends EventHandler<ItemOptionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemOptionEvent event) {
		if (event.getOption() != 2 || event.getInterfaceId() != SynchronizationInventoryListener.INVENTORY_ID) {
			return;
		}

		int inventorySlot = event.getSlot();
		Item equipping = player.getInventory().get(inventorySlot);
		int equippingId = equipping.getId();
		EquipmentDefinition definition = EquipmentDefinition.lookup(equippingId);

		if (definition == null) {
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
			ctx.breakHandlerChain();
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
			ctx.breakHandlerChain();
		}
	}

}