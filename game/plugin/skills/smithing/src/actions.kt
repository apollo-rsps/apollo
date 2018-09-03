import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.Action
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.*
import org.apollo.game.model.Animation
import org.apollo.game.model.Item
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.InterfaceListener
import org.apollo.game.model.inv.Inventory
import org.apollo.game.model.inv.SlottedItem
import org.apollo.game.model.inv.SynchronizationInventoryListener
import org.apollo.game.plugin.api.smithing

class OpenFurnaceAction(
        player: Player,
        pos: Position
) : DistancedAction<Player>(0, true, player, pos, 3) {

    override fun executeAction() {
        //println("Opening UI")
        //Set UI elements
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_BRONZE, Bar.BRONZE.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_IRON, Bar.IRON.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_SILVER, Bar.SILVER.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_STEEL, Bar.STEEL.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_GOLD, Bar.GOLD.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_MITHRIL, Bar.MITHRIL.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_ADAMANT, Bar.ADAMANT.id, 150))
        mob.send(SetWidgetItemModelMessage(Interface.MODEL_RUNEITE, Bar.RUNITE.id, 150))
        //Open UI in chat
        mob.send(OpenDialogueInterfaceMessage(Interface.WIDGET_FURNACE))
        stop()
    }
}

class SmeltingAction(
        player: Player,
        private val bar: Bar,
        private val amount: Int,
        private val pos: Position
) : AsyncAction<Player>(0, true, player) {

    override fun action(): ActionBlock = {
        val leftToSmelt = amount
        if (mob.smithing.current < bar.level) {
            mob.sendMessage("You do not have the required level to smelt this bar.")
            stop()
        }
        while (isRunning && leftToSmelt > 0 && mob.inventory.capacity() > 0) {
            //Check if the player has the ore needed
            for (ore in bar.ores) {
                if (mob.inventory.getAmount(ore.ore.id) < ore.amount) {
                    mob.sendMessage("You have run out of ore to smelt!")
                    stop()
                }
            }

            //start smelt
            mob.playAnimation(Animation(SMELTING_ANIMATION))
            //mob.send(SendPlaySoundMessage(SMELTING_SOUND, 0, 0))
            mob.turnTo(pos)

            //Wait while it is being smelted
            wait(4) //Delay of 4

            //Check if we can add to inv
            if (mob.inventory.add(bar.id)) {
                for (ore in bar.ores) {
                    mob.inventory.remove(ore.ore.id, ore.amount)
                }
                mob.smithing.experience += bar.xp
            } else {
                stop()
            }
        }
        stop()
    }
}

class SmithingAction(
        player: Player,
        private val item: SmithingItem,
        private val amount: Int,
        private val pos: Position
) : AsyncAction<Player>(0, true, player) {

    override fun action(): ActionBlock = {
        val bar = item.bars.bar
        if (!mob.inventory.contains(HAMMER_ITEM)) {
            mob.sendMessage("You need a hammer to start smithing.")
            stop()
        }
        if (mob.smithing.current < bar.level) {
            mob.sendMessage("You do not have the required level to smith this bar.")
            stop()
        }
        if (mob.inventory.getAmount(item.bars.bar.id) < item.bars.amount) {
            mob.sendMessage("You need at least " + item.bars.amount + " bars to make " + ItemDefinition.lookup(item.id).name)
            stop()
        }
        var crafted = 0
        while (crafted < amount) {
            //check bars
            if (mob.inventory.getAmount(item.bars.bar.id) < item.bars.amount) {
                mob.sendMessage("You have run out of bars!")
                stop()
            }
            //Set animation and sound
            mob.playAnimation(Animation(SMITHING_ANIMATION))
            //mob.send(SendPlaySoundMessage(SMITHING_SOUND, 0, 0))
            mob.turnTo(pos)
            //
            wait(4)
            if (mob.inventory.add(item.id, item.makes) == 0) {
                val itemName  = ItemDefinition.lookup(item.id).name
                val barName = ItemDefinition.lookup(item.bars.bar.id).name
                mob.inventory.remove(item.bars.bar.id, item.bars.amount)
                mob.smithing.experience += item.xp
                mob.sendMessage("You hammer the " + barName + " to make " + itemName)
            } else {
                stop()
            }
            //
            crafted++
        }
        //done
        stop()
    }
}

class OpenSmithingAction(
        player: Player,
        private val pos: Position,
        private val bar: Bar
) : DistancedAction<Player>(0, true, player, pos, 1) {

    override fun executeAction() {

        if (!mob.inventory.contains(HAMMER_ITEM)) {
            mob.sendMessage("You need a hammer to start smithing.")
            stop()
            return
        }
        if (mob.smithing.current < bar.level) {
            mob.sendMessage("You do not have the required level to smith this bar.")
            stop()
            return
        }

        //Setup inventory registrations
        var col0: Inventory
        var col1: Inventory
        var col2: Inventory
        var col3: Inventory
        var col4: Inventory

        val playerInvs = findPlayerInvs(mob)
        if (playerInvs == null) {
            col0 = Inventory(5)
            col1 = Inventory(5)
            col2 = Inventory(5)
            col3 = Inventory(5)
            col4 = Inventory(5)
            playersInvs.add(PlayerInvs(mob, col0, col1, col2, col3, col4, position))

        } else {
            col0 = playerInvs.inv0
            col1 = playerInvs.inv1
            col2 = playerInvs.inv2
            col3 = playerInvs.inv3
            col4 = playerInvs.inv4
        }

        //BuildUI
        //println("Opening UI")
        val bars = mob.inventory.getAmount(bar.id)
        //A list of entries in the ui to clear
        val ids = mutableSetOf(
                Interface.TEXT_PLATE_BODY_BARS ,
                Interface.TEXT_PLATE_LEGS_BARS ,
                Interface.TEXT_PLATE_SKIRT_BARS ,
                Interface.TEXT_2H_SWORD_BARS ,
                Interface.TEXT_CLAWS_BARS ,
                Interface.TEXT_KITE_SHIELD_BARS ,
                Interface.TEXT_CHAIN_BODY_BARS ,
                Interface.TEXT_BATTLE_AXE_BARS ,
                Interface.TEXT_WAR_HAMMER_BARS ,
                Interface.TEXT_SQ_SHIELD_BARS ,
                Interface.TEXT_KNIFE_BARS ,
                Interface.TEXT_FULL_HELM_BARS ,
                Interface.TEXT_LONG_SWORD_BARS ,
                Interface.TEXT_SCIMITAR_BARS ,
                Interface.TEXT_ARROW_TIPS_BARS ,
                Interface.TEXT_DART_TIPS_BARS ,
                Interface.TEXT_NAILS_BARS ,
                Interface.TEXT_SWORD_BARS ,
                Interface.TEXT_MED_HELM_BARS ,
                Interface.TEXT_MACE_BARS ,
                Interface.TEXT_AXE_BARS ,
                Interface.TEXT_DAGGER_BARS ,
                Interface.TEXT_OIL_LANTERN_BARS ,
                Interface.TEXT_BULLSEYE_LANTERN_BARS ,
                Interface.TEXT_STUDS_BARS ,
                Interface.TEXT_SPIT_BARS ,
                Interface.TEXT_WIRE_BARS ,
                Interface.TEXT_CANNON_BALL_BARS ,
                Interface.TEXT_PLATE_BODY_NAME ,
                Interface.TEXT_PLATE_LEGS_NAME ,
                Interface.TEXT_PLATE_SKIRT_NAME ,
                Interface.TEXT_2H_SWORD_NAME ,
                Interface.TEXT_CLAWS_NAME ,
                Interface.TEXT_KITE_SHIELD_NAME ,
                Interface.TEXT_CHAIN_BODY_NAME ,
                Interface.TEXT_BATTLE_AXE_NAME ,
                Interface.TEXT_WAR_HAMMER_NAME ,
                Interface.TEXT_SQ_SHIELD_NAME ,
                Interface.TEXT_KNIFE_NAME ,
                Interface.TEXT_FULL_HELM_NAME ,
                Interface.TEXT_LONG_SWORD_NAME ,
                Interface.TEXT_SCIMITAR_NAME ,
                Interface.TEXT_ARROW_TIPS_NAME ,
                Interface.TEXT_DART_TIPS_NAME ,
                Interface.TEXT_NAILS_NAME ,
                Interface.TEXT_SWORD_NAME ,
                Interface.TEXT_MED_HELM_NAME ,
                Interface.TEXT_MACE_NAME ,
                Interface.TEXT_AXE_NAME ,
                Interface.TEXT_DAGGER_NAME ,
                Interface.TEXT_OIL_LANTERN_NAME ,
                Interface.TEXT_BULLSEYE_LANTERN_NAME ,
                Interface.TEXT_STUDS_NAME ,
                Interface.TEXT_SPIT_NAME ,
                Interface.TEXT_WIRE_NAME ,
                Interface.TEXT_CANNON_BALL_NAME
        )
        var itemGrid = Array(5, { BooleanArray(5) })
        //
        //mob.send(SetWidgetTextMessage(1112, b5))
        for (item in SmithingItem.values()) {
            if (item.bars.bar.id == bar.id) {
                val text1: String = if (bars >= item.bars.amount) "@gre@" + item.bars.amount.toString() + "@gre@" else item.bars.amount.toString()
                val text2: String = if (item.bars.amount > 1) "bars" else "bar"
                mob.send(SetWidgetTextMessage(item.barsText, text1 + text2)) //send bars
                var name = ItemDefinition.lookup(item.id).name
                name = name.substring(name.indexOf(" ")+1)
                name = name.replace("2h", "2 hand")
                name = name.replace("med", "medium")
                name = name.replace("knife", "throwing knives")
                name = name.replace("kiteshield", "kite shield")
                name = name.replace("chainbody", "chain body")
                name = name.replace("plate", "plate ")
                name = name.replace("sq", "square")
                name = name.replace("tip", "tips")
                name = name.replace("battleaxe", "battle axe")
                name = name[0].toUpperCase() + name.substring(1)

                val color = if (item.level <= mob.smithing.current) "@whi@" else ""
                mob.send(SetWidgetTextMessage(item.nameText, color + name + color)) //send bars
                //Send item update
                val itm = SlottedItem(item.slotId, Item(item.id, item.makes))
                mob.send(UpdateSlottedItemsMessage(item.columnId, itm))
                //Update used elements
                ids.remove(item.nameText)
                ids.remove(item.barsText)
                //Update item grid
                itemGrid[item.columnId - 1119][item.slotId] = true

                when (item.columnId) {
                    Interface.COLUMN_0 -> col0[item.slotId] = Item(item.id)
                    Interface.COLUMN_1 -> col1[item.slotId] = Item(item.id)
                    Interface.COLUMN_2 -> col2[item.slotId] = Item(item.id)
                    Interface.COLUMN_3 -> col3[item.slotId] = Item(item.id)
                    Interface.COLUMN_4 -> col4[item.slotId] = Item(item.id)
                }
            }
        }
        //clear unused ui widgets
        for (id in ids) {
            mob.send(SetWidgetTextMessage(id, ""))
        }
        //update item grid
        for (column in 0..4) {
            (0..4).filterNot { itemGrid[column][it] }
                    .forEach {
                        //val itm = SlottedItem(row, Item(79, 0))
                        mob.send(UpdateSlottedItemsMessage(column + 1119, SlottedItem(it, null)))
                    }
        }

        //Open UI in client
        mob.interfaceSet.openWindow(addInventoryListeners(mob), Interface.INTERFACE_SMITHING)
        //
        stop()
    }

    private fun addInventoryListeners(player: Player): InterfaceListener {
        val invListener = SynchronizationInventoryListener(player, Interface.INVENTORY_CONTAINER)


        player.inventory.addListener(invListener)
        player.inventory.forceRefresh()

        playersActive.add(player)

        return InterfaceListener {
            mob.interfaceSet.close()
            mob.inventory.removeListener(invListener)
            playersActive.remove(player)
        }
    }
}
