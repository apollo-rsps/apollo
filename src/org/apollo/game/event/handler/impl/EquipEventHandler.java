package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.EquipEvent;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An event handler which equips items.
 * @author Graham
 */
public final class EquipEventHandler extends EventHandler<EquipEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, EquipEvent event) {
		if (event.getInterfaceId() == SynchronizationInventoryListener.INVENTORY_ID) {
			int slot = event.getSlot();
			if (slot < 0 || slot >= player.getInventory().capacity()) {
				ctx.breakHandlerChain();
				return;
			}

			Item item = player.getInventory().get(slot);
			if (item == null || item.getId() != event.getId()) {
				ctx.breakHandlerChain();
				return;
			}

			ItemDefinition itemDef = item.getDefinition();
			EquipmentDefinition equipDef = EquipmentDefinition.forId(item.getId());
			if (equipDef == null) {
				ctx.breakHandlerChain();
				return;
			}

			SkillSet skillSet = player.getSkillSet();
			if (skillSet.getSkill(Skill.ATTACK).getMaximumLevel() < equipDef.getAttackLevel()) {
				player.sendMessage("You need an Attack level of " + equipDef.getAttackLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.STRENGTH).getMaximumLevel() < equipDef.getStrengthLevel()) {
				player.sendMessage("You need a Strength level of " + equipDef.getStrengthLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.DEFENCE).getMaximumLevel() < equipDef.getDefenceLevel()) {
				player.sendMessage("You need a Defence level of " + equipDef.getDefenceLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.RANGED).getMaximumLevel() < equipDef.getRangedLevel()) {
				player.sendMessage("You need a Ranged level of " + equipDef.getRangedLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}
			if (skillSet.getSkill(Skill.MAGIC).getMaximumLevel() < equipDef.getMagicLevel()) {
				player.sendMessage("You need a Magic level of " + equipDef.getMagicLevel() + " to equip this item.");
				ctx.breakHandlerChain();
				return;
			}

			Inventory inventory = player.getInventory();
			Inventory equipment = player.getEquipment();

			int equipmentSlot = equipDef.getSlot();

			// TODO: equip event decoder for 317, and remove event decoder for both
			// TODO: put all this into another method somewhere

			// check if there is enough space for a two handed weapon
			if (equipDef.isTwoHanded()) {
				Item currentShield = equipment.get(EquipmentConstants.SHIELD);
				if (currentShield != null) {
					if (inventory.freeSlots() < 1) {
						inventory.forceCapacityExceeded();
						ctx.breakHandlerChain();
						return;
					}
				}
			}

			// check if a shield is being added with a two handed weapon
			boolean removeWeapon = false;
			if (equipmentSlot == EquipmentConstants.SHIELD) {
				Item currentWeapon = equipment.get(EquipmentConstants.WEAPON);
				if (currentWeapon != null) {
					EquipmentDefinition weaponDef = EquipmentDefinition.forId(currentWeapon.getId());
					if (weaponDef.isTwoHanded()) {
						if (inventory.freeSlots() < 1) {
							inventory.forceCapacityExceeded();
							ctx.breakHandlerChain();
							return;
						}
						removeWeapon = true;
					}
				}
			}

			Item previous = equipment.get(equipmentSlot);
			if (itemDef.isStackable() && previous != null && previous.getId() == item.getId()) {
				// we know the item is there, so we can let the inventory class do its stacking magic
				inventory.remove(item);
				Item tmp = equipment.add(item);
				if (tmp != null) {
					inventory.add(tmp);
				}
			} else {
				// swap the weapons around
				Item tmp = equipment.reset(equipmentSlot);
				equipment.set(equipmentSlot, item);
				inventory.reset(slot);
				if (tmp != null) {
					inventory.add(tmp);
				}
			}

			// remove the shield if this weapon is two handed
			if (equipDef.isTwoHanded()) {
				Item tmp = equipment.reset(EquipmentConstants.SHIELD);
				// we know tmp will not be null from the check above
				inventory.add(tmp);
			}

			if (removeWeapon) {
				Item tmp = equipment.reset(EquipmentConstants.WEAPON);
				// we know tmp will not be null from the check about
				inventory.add(tmp);
			}

			ctx.breakHandlerChain();
		}
	}

}
