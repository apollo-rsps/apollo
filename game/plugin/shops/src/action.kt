package org.apollo.game.plugin.shops

import org.apollo.game.action.DistancedAction
import org.apollo.game.message.handler.ItemVerificationHandler.InventorySupplier
import org.apollo.game.message.impl.SetWidgetTextMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.InterfaceListener
import org.apollo.game.model.inv.Inventory
import org.apollo.game.model.inv.SynchronizationInventoryListener

/**
 * A [DistancedAction] that opens a [Shop].
 */
class OpenShopAction(
    player: Player,
    private val shop: Shop,
    val npc: Mob
) : DistancedAction<Player>(0, true, player, npc.position, 1) { // TODO this needs to follow the NPC if they move

    override fun executeAction() {
        mob.interactingMob = npc

        val closeListener = addInventoryListeners(mob, shop.inventory)
        mob.send(SetWidgetTextMessage(Interfaces.SHOP_NAME, shop.name))

        mob.interfaceSet.openWindowWithSidebar(closeListener, Interfaces.SHOP_WINDOW, Interfaces.INVENTORY_SIDEBAR)
        stop()
    }

    /**
     * Adds [SynchronizationInventoryListener]s to the [Player] and [Shop] [Inventories][Inventory], returning an
     * [InterfaceListener] that removes them when the interface is closed.
     */
    private fun addInventoryListeners(player: Player, shop: Inventory): InterfaceListener {
        val invListener = SynchronizationInventoryListener(player, Interfaces.INVENTORY_CONTAINER)
        val shopListener = SynchronizationInventoryListener(player, Interfaces.SHOP_CONTAINER)

        player.inventory.addListener(invListener)
        player.inventory.forceRefresh()

        shop.addListener(shopListener)
        shop.forceRefresh()

        return InterfaceListener {
            mob.interfaceSet.close()
            mob.resetInteractingMob()

            mob.inventory.removeListener(invListener)
            shop.removeListener(shopListener)
        }
    }
}

/**
 * An [InventorySupplier] that returns a [Player]'s [Inventory] if they are browsing a shop.
 */
class PlayerInventorySupplier : InventorySupplier {

    override fun getInventory(player: Player): Inventory? {
        return when {
            player.interfaceSet.contains(Interfaces.SHOP_WINDOW) -> player.inventory
            else -> null
        }
    }
}