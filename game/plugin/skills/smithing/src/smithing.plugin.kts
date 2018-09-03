import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.*
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inv.Inventory
import org.apollo.game.scheduling.ScheduledTask

start {
    ItemVerificationHandler.addInventory(Interface.COLUMN_0, PlayerInventorySupplier(Interface.COLUMN_0))
    ItemVerificationHandler.addInventory(Interface.COLUMN_1, PlayerInventorySupplier(Interface.COLUMN_1))
    ItemVerificationHandler.addInventory(Interface.COLUMN_2, PlayerInventorySupplier(Interface.COLUMN_2))
    ItemVerificationHandler.addInventory(Interface.COLUMN_3, PlayerInventorySupplier(Interface.COLUMN_3))
    ItemVerificationHandler.addInventory(Interface.COLUMN_4, PlayerInventorySupplier(Interface.COLUMN_4))
}



on { ObjectActionMessage::class }
        .where { furnaces.contains(id) }
        .then {
            //println("Furnace interaction")
            it.startAction(OpenFurnaceAction(it, position))
            waitingForAmount.add(SmeltingWrapper(it, null, position))
            terminate()
        }

on { ButtonMessage::class }
        .where { FurnaceSelection.values().any { it.widget == widgetId } }
        .then {
            val wrapper = getPlayerWaiting(it)
            if (wrapper != null) {
                //close ui for bar selection
                it.send(CloseInterfaceMessage())
                //Open ui for quantaty
                it.send(EnterAmountMessage())
                val bar = FurnaceSelection.values().first { it.widget == widgetId }.bar
                wrapper.bar = bar
                //
                terminate()
            }
        }

on { EnteredAmountMessage::class }
        .then {
            val wrapper = getPlayerWaiting(it)
            if (wrapper != null) {
                waitingForAmount.remove(wrapper)
                //Run smelt action
                it.startAction(SmeltingAction(it, wrapper.bar!!, amount, wrapper.pos))
                //
                terminate()
            }
        }

//Listeners for smithing
on { ItemOnObjectMessage::class }
        .where { Smithable.values().any { it.bar.id == id } && anvils.any{ it == objectId } }
        .then {
            //println("Anvil interaction" + objectId)
            val bar = Bar.values().first { it.id == id }
            it.startAction(OpenSmithingAction(it, position, bar))
            terminate()
        }

on { ItemActionMessage::class }
        .where { interfaceId == Interface.COLUMN_0 || interfaceId == Interface.COLUMN_1 ||interfaceId == Interface.COLUMN_2 ||interfaceId == Interface.COLUMN_3 ||interfaceId == Interface.COLUMN_4}
        .then {
            val amount = amountFromOption(option)
            val item = getSmithingItem(id)
            if (item != null) {
                it.interfaceSet.close()
                val invs = findPlayerInvs(it)!! //Cannot be null at this point
                it.startAction(SmithingAction(it, item, amount, invs.pos))
                terminate()
            }

        }
